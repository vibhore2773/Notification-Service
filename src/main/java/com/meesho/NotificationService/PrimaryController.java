package com.meesho.NotificationService;
import com.meesho.NotificationService.model.*;
import com.meesho.NotificationService.model.apiSmsModel.ApiSmsModel;
import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.response.DataClass;
import com.meesho.NotificationService.model.response.ErrorClass;
import com.meesho.NotificationService.model.response.ResponseSet;
import com.meesho.NotificationService.services.ElasticsearchService;
import com.meesho.NotificationService.services.ImiConnectClient;
import com.meesho.NotificationService.services.SmsDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
public class PrimaryController {
    @Autowired
    SmsDatabaseService smsDatabaseService;
    @Autowired
    KafkaController kafkaController;
    @Autowired
    BlackListRepository blackListRepository;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    ImiConnectClient apiClient;
    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            SmsRequests sms = SmsRequests.builder().phone_number(message.getPhone_number()).message(message.getMessage()).status("Queued for the consumer").build();
            smsDatabaseService.save(sms);
            kafkaController.sendToKafka(String.valueOf(sms.getId()));
            DataClass dataClass = DataClass.builder().request_id(String.valueOf(sms.getId())).comments("Successfully Sent").build();
            return new ResponseEntity<>(new ResponseSet(dataClass), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ApiSmsModel temp(@PathVariable int id){
        SmsRequests sms = smsDatabaseService.findById(id);
        return new ApiSmsModel(sms);
    }

    @GetMapping("/v1/sms/{request_id}")
    public ResponseEntity<ResponseSet> getDetails(@PathVariable("request_id") String id) {
        SmsRequests sms = smsDatabaseService.findById(Integer.parseInt(id));
        if (sms != null) {
            DataClass dataClass = DataClass.builder().request_id(String.valueOf(sms.getId())).message(sms.getMessage()).phone_number(sms.getPhone_number())
                                    .comments(sms.getFailure_comments()).failure_code(sms.getFailure_code()).created_at(sms.getCreated_at()).updated_at(sms.getUpdated_at())
                                    .build();
            return new ResponseEntity(new ResponseSet(dataClass), HttpStatus.OK);
        } else {
            ErrorClass errorClass = new ErrorClass();
            errorClass.setCode("INVALID_REQUEST");
            errorClass.setMessage("request_id Not Found");
            return new ResponseEntity(new ResponseSet(errorClass), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("v1/findsms/{page_number}/{size}")
    public List<Map> findMessage(@RequestBody Message message, @PathVariable("page_number") int page_number, @PathVariable("size") int size) throws IOException {
        return elasticsearchService.findByMessage(message.getMessage(), page_number,size);
    }

    @GetMapping("rangeES")
    public List<Map> dummy() throws IOException {
        SmsRequests sms1 = smsDatabaseService.findById(1);
        SmsRequests sms2 = smsDatabaseService.findById(36);
        return elasticsearchService.findByRange(sms1.getCreated_at(),sms2.getCreated_at(),0,4);
    }
}
