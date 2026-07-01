package com.javanauta.ts.notifier.adapters.in.web.mapper;


import com.javanauta.ts.notifier.adapters.in.web.dto.NotifyTaskRequestDTO;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneId;

@Mapper(componentModel = "spring", imports = { java.time.ZoneId.class, NotificationDTOMapper.class })
public interface NotificationDTOMapper {

    @Mapping(target = "title", source = "name")
    @Mapping(target = "recipient", source = "userEmail")
    @Mapping(target = "timeZoneId", expression = "java(NotificationDTOMapper.convertTimeZoneId(dto))")
    NotifyTaskCommand toCommand(NotifyTaskRequestDTO dto);

    static ZoneId convertTimeZoneId(NotifyTaskRequestDTO dto) {
        return ZoneId.of(dto.timeZoneId());
    }
}
