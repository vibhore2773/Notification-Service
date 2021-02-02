package com.meesho.NotificationService;

import com.meesho.NotificationService.model.Message;
import com.meesho.NotificationService.model.Sms_requests;
import com.meesho.NotificationService.response.DataClass;
import com.meesho.NotificationService.response.ErrorClass;
import com.meesho.NotificationService.response.ResponseSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;

@RestController
public class PrimaryController {
    @Autowired
    SmsRepository repo;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    private static final String TOPIC = "notification.send_sms";
    private ThirdPartyApi api;

    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            Sms_requests sms = new Sms_requests();
            sms.setPhone_number(message.getPhone_number());
            sms.setMessage(message.getMessage());
            sms.setStatus("OK");
            repo.save(sms);
            kafkaTemplate.send(TOPIC,String.valueOf(sms.getId()));
            System.out.println(sms.getId());
//            int i=1/0;
            return new ResponseEntity<>(new ResponseSet(new DataClass()), HttpStatus.OK);
        }catch (Exception E){
            System.out.println(E);
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }

    @KafkaListener(topics = TOPIC, groupId = "group_01")
    public void consumer(String id){
        int Id = Integer.parseInt(id);
        Sms_requests sms = repo.findById(Id).orElse(null);
        api.bhejo(sms);
    }
    @GetMapping("check")
    public ResponseEntity<String> check(){
        return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
    }
}
