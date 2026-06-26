package com.javanauta.ts.notifier.application.port;

import com.javanauta.ts.events.notification.NotificationCompletedEvent;

public interface NotificationCompletedPublisher {
    void publishNotificationCompleted(NotificationCompletedEvent event);
}
