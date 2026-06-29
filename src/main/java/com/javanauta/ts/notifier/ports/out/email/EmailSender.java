package com.javanauta.ts.notifier.ports.out.email;

public interface EmailSender {
    public void send(EmailMessage message);
}
