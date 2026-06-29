package com.javanauta.ts.notifier.ports.out.email;

import com.javanauta.ts.notifier.adapters.out.email.data.EmailMessage;
import com.javanauta.ts.notifier.application.command.NotifyTaskCommand;

public interface EmailComposer {
    public EmailMessage compose(NotifyTaskCommand notifyTaskCommand);
}
