package com.javanauta.ts.notifier.adapters.in.messaging;


import com.javanauta.ts.events.notification.NotificationRequestedEvent;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneId;

@Mapper(componentModel = "spring", imports = { ZoneId.class, NotificationMapper.class })
public interface NotificationMapper {
    @Mapping(target = "id", source = "taskId")
    @Mapping(target = "title", source = "taskName")
    @Mapping(target = "description", source = "taskDescription")
    @Mapping(target = "scheduledDateTime", source = "taskScheduledDateTime")
    @Mapping(target = "recipient", source = "taskRecipient")
    @Mapping(target = "timeZoneId", expression = "java(NotificationMapper.convertTimeZoneId(event))")
    NotifyTaskCommand toCommand(NotificationRequestedEvent event);

    static ZoneId convertTimeZoneId(NotificationRequestedEvent event) {
        return ZoneId.of(event.taskZoneId());
    }
}
