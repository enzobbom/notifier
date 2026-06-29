package com.javanauta.ts.notifier.adapters.out.email.config;

import com.javanauta.ts.notifier.ports.out.email.EmailComposer;
import com.javanauta.ts.notifier.ports.out.email.EmailSender;
import com.javanauta.ts.notifier.adapters.out.email.composer.ThymeleafEmailComposer;
import com.javanauta.ts.notifier.adapters.out.email.sender.NoOpEmailSender;
import com.javanauta.ts.notifier.adapters.out.email.sender.SmtpEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
@EnableConfigurationProperties(EmailProperties.class)
@Slf4j
public class EmailConfig {

    // EmailSender

    @Bean
    @ConditionalOnExpression("'${spring.mail.username:}' != '' && '${spring.mail.password:}' != ''")
    @Primary
    public EmailSender smtpEmailSender(JavaMailSender javaMailSender) {
        return new SmtpEmailSender(javaMailSender);
    }

    @Bean
    @ConditionalOnMissingBean(EmailSender.class)
    public EmailSender noOpEmailSender() {
        log.warn("SMTP server username and password could not be read. NoOpEmailSender will be used");
        return new NoOpEmailSender();
    }

    // EmailComposer

    @Bean
    @Primary
    public EmailComposer thymeleafEmailComposer(TemplateEngine templateEngine, EmailProperties emailProperties) {
        return new ThymeleafEmailComposer(templateEngine, emailProperties);
    }
}
