package com.meesho.NotificationService.services;

import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.Sms_requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;


@Service
public class Consumer {
    ThirdPartyApi api;
    @Autowired
    SmsRepository repo;
    @Autowired
    BlackListRepository blackListRepository;
    @KafkaListener(topics = "notification.send_sms", groupId = "group_id")
    public void consume(String id) throws Exception {
        Sms_requests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        blackListRepository.save(id);
        if(!blackListRepository.exists(id)){
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
