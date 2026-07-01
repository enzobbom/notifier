package com.javanauta.ts.notifier.application.data;

import com.javanauta.ts.notifier.application.data.enums.NotificationResult;

public record NotificationResultDetails(
        String taskId,
        NotificationResult notificationResult,
        String errorMessage
) {
}
