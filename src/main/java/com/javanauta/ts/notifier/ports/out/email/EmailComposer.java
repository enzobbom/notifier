package com.javanauta.ts.notifier.ports.out.email;

import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;

public interface EmailComposer {
    public EmailMessage compose(NotifyTaskCommand notifyTaskCommand);
}
