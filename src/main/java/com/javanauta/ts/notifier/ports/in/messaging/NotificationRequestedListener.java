package com.javanauta.ts.notifier.ports.in.messaging;

import com.javanauta.ts.events.notification.NotificationRequestEvent;

public interface NotificationRequestedListener {
    void handleNotificationRequested(NotificationRequestEvent event);
}
