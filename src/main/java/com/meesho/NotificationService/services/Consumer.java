package com.meesho.NotificationService.services;

import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.SmsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@EnableConfigurationProperties
public class Consumer {
    ThirdPartyApi api;
    @Autowired
    SmsRepository repo;
    @Autowired
    BlackListRepository blackListRepository;



    @KafkaListener(topics = "notification.send_sms", groupId = "group_id",autoStartup = "${kafka.listener.consumer.enabled}")
    public void consume(String id) throws Exception {
        SmsRequests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        blackListRepository.save(sms.getPhone_number());
        if(!blackListRepository.exists(sms.getPhone_number())){
            sms.setFailure_comments("Not in BlackList");
            repo.save(sms);
            api.bhejo(sms);
        }
        else{

            sms.setFailure_comments("Number in black list");
            repo.save(sms);
            System.out.println("Number In Black List");
        }

    }
}
