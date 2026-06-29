package com.javanauta.ts.notifier.ports.in.messaging;

import com.javanauta.ts.events.notification.NotificationRequestedEvent;

public interface NotificationRequestedListener {
    void handleNotificationRequested(NotificationRequestedEvent event);
}
