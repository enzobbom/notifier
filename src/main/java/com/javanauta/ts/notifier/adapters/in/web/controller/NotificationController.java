package com.javanauta.ts.notifier.adapters.in.web.controller;

import com.javanauta.ts.notifier.adapters.in.web.mapper.NotificationDTOMapper;
import com.javanauta.ts.notifier.application.usecase.SendNotificationService;
import com.javanauta.ts.notifier.adapters.in.web.dto.NotifyTaskRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email") // to be renamed to 'notification'
public class NotificationController {

    private final SendNotificationService sendNotificationService;
    private final NotificationDTOMapper notificationDTOMapper;

//    @PostMapping
//    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotifyTaskRequestDTO notifyTaskRequestDTO) {
//        sendNotificationService.sendNotification(notificationDTOMapper.toCommand(notifyTaskRequestDTO));
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<String> sendNotification() {
        return ResponseEntity.status(HttpStatus.GONE).body("End point currently disabled.");
    }
}
