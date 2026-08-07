package com.javanauta.ts.notifier.application.command;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record NotifyTaskCommand(
        String id,
        String title,
        String description,
        Instant scheduledDateTime,
        String recipient,
        ZoneId timeZoneId
) {
    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public String getFormattedScheduledTime() {
        return DateTimeFormatter.ofPattern(DATETIME_FORMAT)
                .withZone(timeZoneId)
                .format(scheduledDateTime);
    }
}
