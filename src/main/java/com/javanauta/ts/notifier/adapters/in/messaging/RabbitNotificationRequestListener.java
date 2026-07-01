package com.javanauta.ts.notifier.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import com.javanauta.ts.notifier.adapters.out.email.exception.enums.EmailExceptionCode;
import com.javanauta.ts.notifier.application.usecase.SendNotificationService;
import com.javanauta.ts.notifier.ports.in.messaging.NotificationRequestedListener;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitNotificationRequestListener implements NotificationRequestedListener {

    private final SendNotificationService notificationService;
    private final NotificationEventMapper notificationEventMapper;

    @RabbitListener(queues = Queues.NOTIFICATION_REQUEST)
    @Override
    public void handleNotificationRequested(NotificationRequestEvent event) {
        try {
            notificationService.sendNotification(notificationEventMapper.toCommand(event));

        } catch (EmailException ex) {
            if (ex.getCode() == EmailExceptionCode.INFRASTRUCTURE_UNAVAILABLE) {
                throw ex;
            } else {
                throw new AmqpRejectAndDontRequeueException(ex);
            }
        }
    }
}
