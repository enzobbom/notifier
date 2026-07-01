package com.javanauta.ts.notifier.adapters.out.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationFailedEvent;
import com.javanauta.ts.events.notification.enums.NotificationFailureType;
import com.javanauta.ts.notifier.application.data.NotificationResultDetails;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationFailedPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RabbitNotificationFailedPublisher implements NotificationFailedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishNotificationFailed(NotificationResultDetails notificationResultDetails) {
        String exchangeName = Exchanges.NOTIFICATION;
        String routingKeyName = RoutingKeys.NOTIFICATION_FAILED;

        NotificationFailureType notificationFailureType = switch (notificationResultDetails.notificationResult()) {
            case PERMANENT_FAILURE -> NotificationFailureType.PERMANENT;
            case TEMPORARY_FAILURE -> NotificationFailureType.TEMPORARY;
            default -> throw new IllegalArgumentException("Only 'FAILURE' types can be used here");
        };

        NotificationFailedEvent event = new NotificationFailedEvent(
                UUID.randomUUID(),
                Instant.now(),
                notificationResultDetails.taskId(),
                notificationFailureType,
                notificationResultDetails.errorMessage()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKeyName,
                event
        );
    }
}
