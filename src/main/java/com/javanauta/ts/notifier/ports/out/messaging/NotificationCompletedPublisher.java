package com.javanauta.ts.notifier.ports.out.messaging;

import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;

public interface NotificationCompletedPublisher {
    void publishNotificationCompleted(NotifyTaskCommand notifyTaskCommand);
}
