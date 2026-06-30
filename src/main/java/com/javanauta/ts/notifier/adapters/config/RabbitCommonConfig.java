package com.javanauta.ts.notifier.adapters.config;

import com.javanauta.ts.notifier.adapters.out.email.exception.EmailException;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitCommonConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        factory.setAdviceChain(retryAdvice());

        return factory;
    }

    @Bean
    public Advice retryAdvice() {

        return RetryInterceptorBuilder.stateless()
                .configureRetryPolicy(builder -> builder
                        .maxRetries(3)
                        .excludes(AmqpRejectAndDontRequeueException.class))
                .backOffOptions(1000, 2.0, 5000)
                .build();
    }
}
