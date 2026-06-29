package com.javanauta.ts.notifier.adapters.in.web.mapper;

import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import com.javanauta.ts.notifier.application.command.enums.NotificationStatus;
import com.javanauta.ts.notifier.adapters.in.web.dto.NotifyTaskRequestDTO;
import java.time.Instant;
import java.time.ZoneId;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-24T19:17:57+0100",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 17.0.17 (Homebrew)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotifyTaskCommand toCommand(NotifyTaskRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        String title = null;
        String recipient = null;
        NotificationStatus status = null;
        String id = null;
        String description = null;
        Instant scheduledDateTime = null;

        title = dto.name();
        recipient = dto.userEmail();
        status = dto.notificationStatus();
        id = dto.id();
        description = dto.description();
        scheduledDateTime = dto.scheduledDateTime();

        ZoneId timeZoneId = NotificationMapper.convertTimeZoneId(dto);

        NotifyTaskCommand notifyTaskCommand = new NotifyTaskCommand( id, title, description, scheduledDateTime, recipient, status, timeZoneId );

        return notifyTaskCommand;
    }
}
