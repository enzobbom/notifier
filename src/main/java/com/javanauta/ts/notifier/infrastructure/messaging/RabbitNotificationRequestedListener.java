package com.javanauta.ts.notifier.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import com.javanauta.ts.notifier.application.port.NotificationRequestedListener;
import com.javanauta.ts.notifier.application.usecase.SendNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationRequestedListener implements NotificationRequestedListener {

    private final SendNotificationService notificationService;

    @RabbitListener(queues = Queues.NOTIFICATION_REQUEST)
    @Override
    public void handleNotificationRequested(NotificationRequestedEvent event) {
        log.info(
                "Handling NotificationRequestedEvent for Task '{}'",
                event.taskId()
        );

        NotifyTaskCommand command = new NotifyTaskCommand(
                event.taskId(),
                event.taskName(),
                event.taskDescription(),
                event.taskScheduledDateTime(),
                event.taskRecipient(),
                ZoneId.of(event.taskZoneId())
        );

        notificationService.sendNotification(command);

        log.info("Handled successfully");
    }
}
