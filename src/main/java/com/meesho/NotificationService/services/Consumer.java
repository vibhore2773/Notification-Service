package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.SmsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@EnableConfigurationProperties
public class Consumer {
    ImiConnectClient clientApi;
    @Autowired
    SmsRepository repo;
    @Autowired
    BlackListRepository blackListRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @KafkaListener(topics = "notification.send_sms", groupId = "group_id",autoStartup ="${kafka.listener.consumer.enabled}")
    public void consume(String id) throws Exception {
        SmsRequests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        if(!blackListRepository.exists(sms.getPhone_number())){

            //UPDATING SQL DB
            sms.setStatus("Received by consumer");
            sms.setFailure_comments("Not in BlackList");
            sms.setUpdated_at(new Date());
            repo.save(sms);

            //STORING IN ELASTIC SEARCH
            SearchData data = SearchData.builder()
                                .id(String.valueOf(sms.getId()))
                                .message(sms.getMessage())
                                .phone_number(sms.getPhone_number())
                                .created_at(sms.getCreated_at())
                                .build();
            elasticsearchOperations.save(data);

            //SENDING SMS REQUEST
            String apiResponse = clientApi.sendMessage(sms);

        }
        else{
            sms.setStatus("Received by consumer");
            sms.setFailure_comments("Number in black list");
            sms.setUpdated_at(new Date());
            repo.save(sms);
            System.out.println("Number In Black List");
        }

    }
}
