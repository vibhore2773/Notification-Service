package com.meesho.NotificationService;

import com.meesho.NotificationService.services.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaController {

    private final Producer producer;

    @Autowired
    public KafkaController(Producer producer) {
        this.producer = producer;
    }

    public void sendToKafka(String id){

        this.producer.sendMessage(id);
    }
}
