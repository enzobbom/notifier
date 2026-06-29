package com.javanauta.ts.notifier.adapters.out.email.data;

import lombok.Builder;

@Builder
public record EmailMessage(
        String sender,
        String senderName,
        String recipient,
        String subject,
        String body
) {}

