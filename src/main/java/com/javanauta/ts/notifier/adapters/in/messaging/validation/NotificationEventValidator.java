package com.javanauta.ts.notifier.adapters.in.messaging.validation;

import com.javanauta.ts.events.notification.Event;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationEventValidator {
    private final Validator validator;

    public <T extends Event> void validate(T event) {
        Set<ConstraintViolation<T>> violations = validator.validate(event);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
