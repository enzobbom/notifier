package com.javanauta.ts.notifier.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.notifier.application.usecase.SendNotificationService;
import com.javanauta.ts.notifier.ports.in.messaging.NotificationRequestedListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationRequestedListener implements NotificationRequestedListener {

    private final SendNotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @RabbitListener(queues = Queues.NOTIFICATION_REQUEST)
    @Override
    public void handleNotificationRequested(NotificationRequestedEvent event) {
        log.info(
                "Handling NotificationRequestedEvent for Task '{}'",
                event.taskId()
        );

        notificationService.sendNotification(notificationMapper.toCommand(event));

        log.info("Handled successfully");
    }
}
