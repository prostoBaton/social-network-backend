package com.example.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    public ConnectionFactory connectionFactory(){
        return new CachingConnectionFactory("rabbitMQ");
    }

    @Bean
    public AmqpAdmin amqpAdmin(){
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue publicationsQueue(){
        return new Queue("publications");
    }

    @Bean
    public Queue activationsQueue(){
        return new Queue("activations");
    }

    @Bean
    public TopicExchange publicationsExchange(){
        return new TopicExchange("publications_exchange");
    }

    @Bean
    public TopicExchange activationsExchange(){
        return new TopicExchange("activations_exchange");
    }

    @Bean
    public Binding publicationsBinding(){
        return BindingBuilder
                .bind(publicationsQueue())
                .to(publicationsExchange())
                .with("publications_routing");
    }

    @Bean
    public Binding activationsBinding(){
        return BindingBuilder
                .bind(activationsQueue())
                .to(activationsExchange())
                .with("activations_routing");
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
