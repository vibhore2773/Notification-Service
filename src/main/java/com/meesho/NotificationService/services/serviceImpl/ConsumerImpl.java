package com.meesho.NotificationService.services.serviceImpl;

import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.SmsRequests;
import com.meesho.NotificationService.services.Consumer;
import com.meesho.NotificationService.services.ImiConnectClient;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@EnableConfigurationProperties
public class ConsumerImpl implements Consumer {
    @Autowired
    ImiConnectClient clientApi;
    @Autowired
    SmsRepository repo;
    @Autowired
    BlackListRepository blackListRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @KafkaListener(topics = "${kafka-topic}", groupId = "${spring.kafka.consumer.group-id}",autoStartup ="${kafka.listener.consumer.enabled}")
    public void consume(String id) throws Exception {
        SmsRequests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        if(!blackListRepository.exists(sms.getPhoneNumber())){

            //UPDATING SQL DB
            sms.setStatus("Received by consumer");
            sms.setFailureComments("Not in BlackList");
            sms.setUpdatedAt(new Date());
            repo.save(sms);

            //STORING IN ELASTIC SEARCH
            SearchData data = SearchData.builder()
                                .id(String.valueOf(sms.getId()))
                                .message(sms.getMessage())
                                .phoneNumber(sms.getPhoneNumber())
                                .createdAt(sms.getCreatedAt())
                                .build();
            elasticsearchOperations.save(data);

            //SENDING SMS REQUEST
            String apiResponse = clientApi.sendMessage(sms);

        }
        else{
            sms.setStatus("Received by consumer");
            sms.setFailureComments("Number in black list");
            sms.setUpdatedAt(new Date());
            repo.save(sms);
            System.out.println("Number In Black List");
        }

    }
}
