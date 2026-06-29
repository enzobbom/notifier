package com.javanauta.ts.notifier.ports.out.email;

import com.javanauta.ts.notifier.adapters.out.email.data.EmailMessage;

public interface EmailSender {
    public void send(EmailMessage message);
}
