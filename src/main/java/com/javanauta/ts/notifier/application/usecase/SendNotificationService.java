package com.javanauta.ts.notifier.application.usecase;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.events.notification.enums.NotificationResult;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationCompletedPublisher;
import com.javanauta.ts.notifier.ports.out.email.EmailComposer;
import com.javanauta.ts.notifier.ports.out.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotificationService {
    private final EmailComposer emailComposer;
    private final EmailSender emailSender;
    private final NotificationCompletedPublisher notificationCompletedPublisher;

    public void sendNotification(NotifyTaskCommand notifyTaskCommand) {
        sendEmailNotification(notifyTaskCommand);

        NotificationCompletedEvent event = new NotificationCompletedEvent(
                UUID.randomUUID(),
                Instant.now(),
                notifyTaskCommand.id(),
                NotificationResult.SUCCESS,
                ""
        );
        notificationCompletedPublisher.publishNotificationCompleted(event);
    }

    private void sendEmailNotification(NotifyTaskCommand notifyTaskCommand) {
        emailSender.send(emailComposer.compose(notifyTaskCommand));
        log.info("Task '{}' was successfully notified by email", notifyTaskCommand.id());
    }
}
