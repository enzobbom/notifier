package com.javanauta.ts.notifier.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.notifier.application.port.NotificationRequestedListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = Queues.NOTIFICATION_REQUEST)
public class RabbitNotificationRequestedListener implements NotificationRequestedListener {
    public void notify(NotificationRequestedEvent event) {
    }
}
