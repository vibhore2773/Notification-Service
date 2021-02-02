package com.meesho.NotificationService.services;

import com.meesho.NotificationService.SmsRepository;
import com.meesho.NotificationService.ThirdPartyApi;
import com.meesho.NotificationService.model.Sms_requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class Consumer {
    ThirdPartyApi api;
    @Autowired
    SmsRepository repo;
    @KafkaListener(topics = "users", groupId = "group_01")
    public void consume(String id) throws IOException {
        Sms_requests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        api.bhejo(sms);
    }
}
