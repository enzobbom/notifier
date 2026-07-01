package com.javanauta.ts.notifier.ports.out.messaging;

import com.javanauta.ts.notifier.application.data.NotificationResultDetails;

public interface NotificationCompletedPublisher {
    void publishNotificationCompleted(NotificationResultDetails resultDetails);
}
