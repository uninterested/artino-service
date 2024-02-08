package com.artino.service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /**
     * 队列
     */
    private final String queue = "KQUEUE";
    /**
     * 交换机
     */
    private final String exchange = "KEXCHANGE";

    @Bean
    public Queue queueBean() {
        return new Queue(queue);
    }

    @Bean
    public Exchange exchangeBean() {
        return ExchangeBuilder.fanoutExchange(exchange).durable(true).build();
    }

    @Bean
    public Binding bindBean(Exchange exchange, Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}