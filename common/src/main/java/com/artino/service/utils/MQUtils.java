package com.artino.service.utils;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MQUtils {

    private final static RabbitTemplate rabbitTemplate;
    static  {
        rabbitTemplate = SpringUtils.getBean(RabbitTemplate.class);
    }

    private static final String exchange = "exchange";

    /**
     * 发送rabbitmq消息
     * @param message
     */
    public static void publishMessage(String message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(CryptoUtils.randomKey());
        rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
    }
}
