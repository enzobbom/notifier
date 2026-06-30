package com.javanauta.ts.notifier.adapters.out.email.sender;

import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import com.javanauta.ts.notifier.adapters.out.email.data.EmailMessage;
import com.javanauta.ts.notifier.adapters.out.email.exception.enums.EmailExceptionCode;
import com.javanauta.ts.notifier.ports.out.email.EmailSender;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;

    public void send(EmailMessage messageToSend) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom(new InternetAddress(messageToSend.sender(), messageToSend.senderName()));
            mimeMessageHelper.setTo(InternetAddress.parse(messageToSend.recipient()));
            mimeMessageHelper.setSubject(messageToSend.subject());
            mimeMessageHelper.setText(messageToSend.body(), true);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailException(
                    EmailExceptionCode.INTERNAL_ERROR,
                    ex);
        }

        try {
            javaMailSender.send(mimeMessage);

        } catch (MailException ex) {
            if (isConnectionException(ex)) {
                throw new EmailException(EmailExceptionCode.INFRASTRUCTURE_UNAVAILABLE, ex);
            } else {
                throw new EmailException(EmailExceptionCode.INTERNAL_ERROR, ex);
            }
        }
    }

    private boolean isConnectionException(Throwable ex) {
        while (ex != null) {
            if (ex instanceof ConnectException
                    || ex instanceof SocketTimeoutException
                    || ex instanceof UnknownHostException) {
                return true;

            } else if (ex instanceof AuthenticationFailedException
                    || ex instanceof SendFailedException) {
                return false;
            }

            ex = ex.getCause();
        }

        return false;
    }
}
