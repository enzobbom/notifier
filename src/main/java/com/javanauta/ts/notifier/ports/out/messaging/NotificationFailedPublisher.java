package com.javanauta.ts.notifier.ports.out.messaging;

import com.javanauta.ts.notifier.application.data.NotificationResultDetails;

public interface NotificationFailedPublisher {
    void publishNotificationFailed(NotificationResultDetails notificationResultDetails);
}
