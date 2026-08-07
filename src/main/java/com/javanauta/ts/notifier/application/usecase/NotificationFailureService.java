package com.javanauta.ts.notifier.application.usecase;

import com.javanauta.ts.notifier.application.data.NotificationResultDetails;
import com.javanauta.ts.notifier.ports.out.messaging.NotificationFailedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFailureService {
    private final NotificationFailedPublisher notificationFailedPublisher;

    public void handleNotificationFailure(NotificationResultDetails notificationResultDetails) {
        notificationFailedPublisher.publishNotificationFailed(notificationResultDetails);

        log.info("Notification failure was handled for Task '{}'", notificationResultDetails.taskId());
    }
}
