package com.javanauta.ts.notifier.adapters.in.messaging.config;

import com.javanauta.ts.notifier.adapters.in.messaging.NotificationFailedRecoverer;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitInConfig {
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter,
            Advice retryAdvice) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        factory.setAdviceChain(retryAdvice);

        return factory;
    }

    @Bean
    public Advice retryAdvice(NotificationFailedRecoverer notificationFailedRecoverer) {
        return RetryInterceptorBuilder.stateless()
                .configureRetryPolicy(builder -> builder
                        .maxRetries(3)
                        .excludes(
                                MessageConversionException.class,
                                AmqpRejectAndDontRequeueException.class))
                .backOffOptions(60000, 1.0, 60000)
                .recoverer(notificationFailedRecoverer)
                .build();
    }
}
