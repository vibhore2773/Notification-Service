package com.meesho.NotificationService;

import com.meesho.NotificationService.model.*;
import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.model.response.DataClass;
import com.meesho.NotificationService.model.response.ErrorClass;
import com.meesho.NotificationService.model.response.ResponseSet;
import com.meesho.NotificationService.services.ElasticsearchService;
import com.meesho.NotificationService.services.ImiConnectClient;
import com.meesho.NotificationService.services.Producer;
import com.meesho.NotificationService.services.SmsDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class PrimaryController {
    @Autowired
    SmsDatabaseService smsDatabaseService;
    @Autowired
    Producer producer;
    @Autowired
    ElasticsearchService elasticsearchService;
    @Autowired
    ImiConnectClient apiClient;

    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            SmsRequests sms = SmsRequests.builder().phoneNumber(message.getPhone_number()).message(message.getMessage()).status("Queued for the consumer").build();
            smsDatabaseService.save(sms);
            producer.sendMessage(String.valueOf(sms.getId()));
            DataClass dataClass = DataClass.builder().request_id(String.valueOf(sms.getId())).comments("Successfully Sent").build();
            return new ResponseEntity<>(new ResponseSet(dataClass), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/v1/sms/{request_id}")
    public ResponseEntity<ResponseSet> getDetails(@PathVariable("request_id") String id) {
        SmsRequests sms = smsDatabaseService.findById(Integer.parseInt(id));
        if (sms != null) {
            DataClass dataClass = DataClass.builder().request_id(String.valueOf(sms.getId())).message(sms.getMessage()).phone_number(sms.getPhoneNumber())
                                    .comments(sms.getFailureComments()).failure_code(sms.getFailureCode()).created_at(sms.getCreatedAt()).updated_at(sms.getUpdatedAt())
                                    .build();
            return new ResponseEntity(new ResponseSet(dataClass), HttpStatus.OK);
        } else {
            ErrorClass errorClass = new ErrorClass();
            errorClass.setCode("INVALID_REQUEST");
            errorClass.setMessage("request_id Not Found");
            return new ResponseEntity(new ResponseSet(errorClass), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("v1/findSms/")
    public List<SearchData> findMessage(@RequestBody Message message) throws IOException {
        return elasticsearchService.findByMessage(message.getMessage(), message.getPageNumber(),message.getSize());
    }

    @GetMapping("/findSmsBetweenDates/")
    public List<SearchData> findMessageByRange(@RequestBody DateRange range) throws IOException {
        return elasticsearchService.findByRange(range.getFrom(),range.getTo(),range.getPageNumber(),range.getSize());
    }
}
