package com.javanauta.ts.notifier.adapters.in.messaging;

import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.notification.NotificationRequestEvent;
import com.javanauta.ts.notifier.adapters.in.messaging.mapper.NotificationEventMapper;
import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import com.javanauta.ts.notifier.adapters.out.email.exception.enums.EmailExceptionCode;
import com.javanauta.ts.notifier.application.usecase.SendNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitNotificationRequestListener {

    private final SendNotificationService notificationService;
    private final NotificationEventMapper notificationEventMapper;

    @RabbitListener(queues = Queues.NOTIFICATION_REQUEST)
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
