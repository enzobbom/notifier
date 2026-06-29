package com.javanauta.ts.notifier.adapters.out.messaging;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.RoutingKeys;
import com.javanauta.ts.events.notification.NotificationCompletedEvent;
import com.javanauta.ts.events.notification.enums.NotificationResult;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationCompletedPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitNotificationCompletedPublisher implements NotificationCompletedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishNotificationCompleted(NotifyTaskCommand notifyTaskCommand) {
        String exchangeName = Exchanges.NOTIFICATION;
        String routingKeyName = RoutingKeys.NOTIFICATION_COMPLETED;

        NotificationCompletedEvent event = new NotificationCompletedEvent(
                UUID.randomUUID(),
                Instant.now(),
                notifyTaskCommand.id(),
                NotificationResult.SUCCESS,
                ""
        );

        log.info(
                "Publishing NotificationCompletedEvent '{}' to exchange [{}] with routing key [{}] for Task '{}'",
                event.eventId(),
                exchangeName,
                routingKeyName,
                event.taskId()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKeyName,
                event
        );

        log.info("Published successfully");
    }
}
