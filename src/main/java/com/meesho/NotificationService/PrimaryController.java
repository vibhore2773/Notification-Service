package com.meesho.NotificationService;

import com.meesho.NotificationService.model.Message;
import com.meesho.NotificationService.model.Sms_requests;
import com.meesho.NotificationService.response.DataClass;
import com.meesho.NotificationService.response.ErrorClass;
import com.meesho.NotificationService.response.ResponseSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PrimaryController {
    @Autowired
    SmsRepository repo;
    @Autowired
    KafkaController kafkaController;

    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            Sms_requests sms = new Sms_requests();
            sms.setPhone_number(message.getPhone_number());
            sms.setMessage(message.getMessage());
            sms.setStatus("OK");
            repo.save(sms);
            kafkaController.sendToKafka(String.valueOf(sms.getId()));
            return new ResponseEntity<>(new ResponseSet(new DataClass()), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }
}
