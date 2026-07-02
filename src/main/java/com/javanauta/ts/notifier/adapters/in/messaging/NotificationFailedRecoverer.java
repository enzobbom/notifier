package com.javanauta.ts.notifier.adapters.in.messaging;

import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import com.javanauta.ts.notifier.application.data.NotificationResultDetails;
import com.javanauta.ts.notifier.application.data.enums.NotificationResult;
import com.javanauta.ts.notifier.application.usecase.NotificationFailureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationFailedRecoverer implements MessageRecoverer {
    private final MessageConverter messageConverter;
    private final NotificationFailureService notificationFailureService;

    @Override
    public void recover(Message message, Throwable cause) {
        NotificationRequestEvent event = (NotificationRequestEvent) messageConverter.fromMessage(message);
        log.error("Notification for Task {} has failed", event.taskId(), cause);

        Throwable underlyingCause = getUnderlyingCause(cause);

        NotificationResult result;
        if (underlyingCause instanceof EmailException emailException) {
            result = switch (emailException.getCode()) {
                case INFRASTRUCTURE_UNAVAILABLE -> NotificationResult.TEMPORARY_FAILURE;
                case INTERNAL_ERROR -> NotificationResult.PERMANENT_FAILURE;
            };
        } else {
            result = NotificationResult.PERMANENT_FAILURE;
        }

        notificationFailureService.handleNotificationFailure(
                new NotificationResultDetails(
                        event.taskId(),
                        result,
                        underlyingCause.getMessage()
                )
        );
    }

    private Throwable getUnderlyingCause(Throwable cause) {
        Throwable currentCause = cause;
        while (currentCause.getCause() != null) {
            currentCause = currentCause.getCause();
        }
        return currentCause;
    }
}
