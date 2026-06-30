package com.javanauta.ts.notifier.adapters.out.email.exception;

import com.javanauta.ts.notifier.adapters.out.email.exception.enums.EmailExceptionCode;
import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private final EmailExceptionCode code;
    private final String message;
    private final Throwable originalCause;

    public EmailException(EmailExceptionCode code, String message, Throwable originalCause) {
        this.code = code;
        if (message == null || message.isBlank()) {
            this.message = code.getDefaultMessage();
        } else {
            this.message = message;
        }
        this.originalCause = originalCause;
    }

    public EmailException(EmailExceptionCode code, Throwable origEx) {
        this(code, "", origEx);
    }
}
