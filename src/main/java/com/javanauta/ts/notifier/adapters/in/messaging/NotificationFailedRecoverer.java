package com.javanauta.ts.notifier.adapters.in.messaging;

import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import com.javanauta.ts.notifier.application.data.NotificationResultDetails;
import com.javanauta.ts.notifier.application.data.enums.NotificationResult;
import com.javanauta.ts.notifier.application.usecase.NotificationFailureService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationFailedRecoverer implements MessageRecoverer {
    private final MessageConverter messageConverter;
    private final NotificationFailureService notificationFailureService;

    @Override
    public void recover(Message message, Throwable cause) {
        // Pre-listener conversion failure
        if (findCause(cause, MessageConversionException.class) != null) {
            log.error("""
                    Invalid NotificationRequestEvent received.
                    Payload:
                    {}
                    """, new String(message.getBody(), StandardCharsets.UTF_8), cause);
            return;
        }

        NotificationRequestEvent event;
        try {
            event = (NotificationRequestEvent) messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            log.error("""
                    Attempt to deserialize NotificationRequestEvent within the Recoverer failed.
                    Payload:
                    {}
                    """, new String(message.getBody(), StandardCharsets.UTF_8), e);
            return;
        }

        // Validation errors
        ConstraintViolationException constraintViolationException = findCause(cause, ConstraintViolationException.class);
        if (constraintViolationException != null) {
            List<String> constraintMessages = constraintViolationException
                    .getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .toList();

            log.error("Validation of NotificationRequestEvent for Task {} has failed: {}",
                    event.taskId(),
                    String.join(", ", constraintMessages));

            return;
        }

        log.error("Notification for Task {} has failed", event.taskId(), cause);

        // Business error
        NotificationResult result;
        String errorMsg;
        EmailException emailException = findCause(cause, EmailException.class);
        if (emailException != null) {
            result = switch (emailException.getCode()) {
                case INFRASTRUCTURE_UNAVAILABLE -> NotificationResult.TEMPORARY_FAILURE;
                case INTERNAL_ERROR -> NotificationResult.PERMANENT_FAILURE;
            };
            errorMsg = emailException.getMessage();

        } else { // Any other unhandled exception
            result = NotificationResult.PERMANENT_FAILURE;
            errorMsg = "Notification could not be sent due to an internal error";
        }

        notificationFailureService.handleNotificationFailure(
                new NotificationResultDetails(
                        event.taskId(),
                        result,
                        errorMsg));
    }

    private <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
        while (throwable != null) {
            if (type.isInstance(throwable)) {
                return type.cast(throwable);
            }
            throwable = throwable.getCause();
        }
        return null;
    }
}
