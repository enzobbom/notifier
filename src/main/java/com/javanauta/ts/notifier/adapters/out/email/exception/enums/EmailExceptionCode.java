package com.javanauta.ts.notifier.adapters.out.email.exception.enums;

public enum EmailExceptionCode {
    INFRASTRUCTURE_UNAVAILABLE ("Error sending e-mail. Infrastructure is currently unavailable"),
    INTERNAL_ERROR ("Error sending e-mail. An internal error occurred");

    private final String defaultMessage;

    EmailExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
