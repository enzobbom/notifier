package com.javanauta.ts.notifier.infrastructure.config;

import com.javanauta.ts.events.messaging.Exchanges;
import com.javanauta.ts.events.messaging.Queues;
import com.javanauta.ts.events.messaging.RoutingKeys;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

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
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
