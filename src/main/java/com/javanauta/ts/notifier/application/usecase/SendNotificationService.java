package com.javanauta.ts.notifier.application.usecase;

import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import com.javanauta.ts.notifier.ports.out.email.EmailComposer;
import com.javanauta.ts.notifier.ports.out.email.EmailSender;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationCompletedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotificationService {
    private final EmailComposer emailComposer;
    private final EmailSender emailSender;
    private final NotificationCompletedPublisher notificationCompletedPublisher;

    public void sendNotification(NotifyTaskCommand notifyTaskCommand) {
        sendEmailNotification(notifyTaskCommand);
        notificationCompletedPublisher.publishNotificationCompleted(notifyTaskCommand);
    }

    private void sendEmailNotification(NotifyTaskCommand notifyTaskCommand) {
        emailSender.send(emailComposer.compose(notifyTaskCommand));
        log.info("Task '{}' was successfully notified by email", notifyTaskCommand.id());
    }
}
