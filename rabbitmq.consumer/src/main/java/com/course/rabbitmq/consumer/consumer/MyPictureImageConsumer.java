package com.course.rabbitmq.consumer.consumer;

import com.course.rabbitmq.consumer.entity.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

//@Service
public class MyPictureImageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MyPictureImageConsumer.class);
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "q.mypicture.image")
    public void listen(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        Picture picture = objectMapper.readValue(message, Picture.class);
        if(picture.getSize() > 9000){
            channel.basicReject(tag, false);
            return;
//            throw new AmqpRejectAndDontRequeueException("picture too large : " + picture);
        }
        log.info("On image : {} ", picture);
        channel.basicAck(tag, false);
    }
}
