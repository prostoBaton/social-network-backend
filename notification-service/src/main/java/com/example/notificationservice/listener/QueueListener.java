package com.example.notificationservice.listener;

import com.example.notificationservice.dto.RabbitDto;
import com.example.notificationservice.sender.EmailSender;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class QueueListener {

    private final EmailSender emailSender;

    public QueueListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "publications")
    public void listenPub(RabbitDto rabbitDto){
        System.out.println(rabbitDto.getMessage());
        System.out.println(rabbitDto.getEmail());
        emailSender.send(rabbitDto.getEmail(), rabbitDto.getMessage());
    }

    @RabbitListener(queues = "activations")
    public void listenAct(RabbitDto rabbitDto){
        System.out.println(rabbitDto.getMessage());
        System.out.println(rabbitDto.getEmail());
        emailSender.send(rabbitDto.getEmail(), rabbitDto.getMessage());
    }
}
