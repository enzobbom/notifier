package com.javanauta.ts.notifier.adapters.in.messaging.config;

import com.javanauta.ts.events.notification.messaging.Exchanges;
import com.javanauta.ts.events.notification.messaging.Queues;
import com.javanauta.ts.events.notification.messaging.RoutingKeys;
import com.javanauta.ts.notifier.adapters.in.messaging.NotificationFailedRecoverer;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    public TopicExchange notificationExchange() {
        return new TopicExchange(Exchanges.NOTIFICATION);
    }

    @Bean
    public Queue notificationRequestQueue() {
        return new Queue(Queues.NOTIFICATION_REQUEST);
    }

    @Bean
    public Binding notificationBinding(
            Queue notificationRequestQueue,
            TopicExchange notificationExchange) {

        return BindingBuilder
                .bind(notificationRequestQueue)
                .to(notificationExchange)
                .with(RoutingKeys.NOTIFICATION_REQUEST);
    }

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
