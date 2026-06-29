package com.javanauta.ts.notifier.adapters.out.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "sending.email")
public class EmailProperties {
    private String sender;
    private String senderName;
}
