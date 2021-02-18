package com.meesho.NotificationService.services.serviceImpl;

import com.meesho.NotificationService.services.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerImpl implements Producer {
    @Value("${kafka-topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String id) {
        this.kafkaTemplate.send(topic, id);
    }
}
