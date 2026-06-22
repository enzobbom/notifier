package com.javanauta.ts.notifier.infrastructure.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.notifier.application.port.NotificationCompletedPublisher;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@AllArgsConstructor
public class RabbitNotificationCompletedPublisher implements NotificationCompletedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(NotificationCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                Exchanges.NOTIFICATION,
                RoutingKeys.NOTIFICATION_COMPLETED,
                event
        );
    }
}
