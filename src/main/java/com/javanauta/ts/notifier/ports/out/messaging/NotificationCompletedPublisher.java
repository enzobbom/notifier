package com.javanauta.ts.notifier.ports.out.messaging;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;

public interface NotificationCompletedPublisher {
    void publishNotificationCompleted(NotificationCompletedEvent event);
}
