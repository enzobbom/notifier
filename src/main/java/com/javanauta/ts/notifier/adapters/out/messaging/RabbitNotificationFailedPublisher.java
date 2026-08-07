package com.javanauta.ts.notifier.adapters.out.messaging;

import com.javanauta.ts.events.notification.NotificationFailedEvent;
import com.javanauta.ts.events.notification.enums.NotificationFailureType;
import com.javanauta.ts.events.notification.messaging.Exchanges;
import com.javanauta.ts.events.notification.messaging.RoutingKeys;
import com.javanauta.ts.notifier.application.data.NotificationResultDetails;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationFailedPublisher;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitNotificationFailedPublisher implements NotificationFailedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishNotificationFailed(NotificationResultDetails notificationResultDetails) {
        NotificationFailureType notificationFailureType = switch (notificationResultDetails.notificationResult()) {
            case PERMANENT_FAILURE -> NotificationFailureType.PERMANENT;
            case TEMPORARY_FAILURE -> NotificationFailureType.TEMPORARY;
            default -> throw new IllegalArgumentException("Only 'FAILURE' results are expected here");
        };

        NotificationFailedEvent event = NotificationFailedEvent.create(
                notificationResultDetails.taskId(),
                notificationFailureType,
                notificationResultDetails.errorMessage()
        );

        rabbitTemplate.convertAndSend(
                Exchanges.NOTIFICATION,
                RoutingKeys.NOTIFICATION_FAILED,
                event
        );
    }
}
