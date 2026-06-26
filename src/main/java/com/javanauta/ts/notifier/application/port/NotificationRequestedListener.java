package com.javanauta.ts.notifier.application.port;

import com.javanauta.ts.events.notification.NotificationRequestedEvent;

public interface NotificationRequestedListener {
    void handleNotificationRequested(NotificationRequestedEvent event);
}
