package com.meesho.NotificationService;
import com.meesho.NotificationService.model.Blacklist;
import com.meesho.NotificationService.model.Message;
import com.meesho.NotificationService.model.PhoneNumber;
import com.meesho.NotificationService.model.SmsRequests;
import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.response.DataClass;
import com.meesho.NotificationService.response.ErrorClass;
import com.meesho.NotificationService.response.ResponseSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Set;


@RestController
public class PrimaryController {
    @Autowired
    SmsRepository repo;
    @Autowired
    KafkaController kafkaController;
    @Autowired
    BlackListRepository blackListRepository;
    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            SmsRequests sms = new SmsRequests();
            sms.setPhone_number(message.getPhone_number());
            sms.setMessage(message.getMessage());
            sms.setStatus("OK");
            repo.save(sms);
            kafkaController.sendToKafka(String.valueOf(sms.getId()));
            DataClass dataClass = new DataClass();
            dataClass.setRequest_id(String.valueOf(sms.getId()));
            dataClass.setComments("Successfully Sent");

            return new ResponseEntity<>(new ResponseSet(dataClass), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/v1/blacklist")
    public ResponseEntity<ResponseSet> addToBlacklist(@RequestBody PhoneNumber phoneNumber){
        try{
            for(String number : phoneNumber.getPhone_numbers()){
                blackListRepository.save(number);
            }
            DataClass dataClass = new DataClass();
            dataClass.setMessage("Successfully Blacklisted");
            return new ResponseEntity(new ResponseSet(dataClass),HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity(new ResponseSet(new DataClass()),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/exists/{number}")
    public String exists(@PathVariable("number") String number){
        boolean check = blackListRepository.exists(number);
        return String.valueOf(check);
    }
    @GetMapping("/v1/blacklist")
    public ResponseEntity<Set<String>> getBlacklist(){
        Set<String> data = blackListRepository.findAll();

        return new ResponseEntity(data, HttpStatus.OK);
    }
    @GetMapping("v1/deleteAll")
    public String deleteAll(){
        return String.valueOf(blackListRepository.deleteAll());

    }
    @DeleteMapping("v1/blacklist/delete/")
    public String delete(@RequestBody String phoneNumber){
        blackListRepository.delete(phoneNumber);
        return "Success";
    }
    @GetMapping("/v1/sms/{request_id}")
    public ResponseEntity<ResponseSet> getDetails(@PathVariable("request_id") String id) {
        SmsRequests sms = repo.findById(Integer.parseInt(id)).orElse(null);
        if (sms != null) {
            DataClass dataClass = new DataClass();
            dataClass.setDetails(sms);
            return new ResponseEntity(new ResponseSet(dataClass), HttpStatus.OK);
        } else {
            ErrorClass errorClass = new ErrorClass();
            errorClass.setCode("INVALID_REQUEST");
            errorClass.setMessage("request_id Not Found");
            return new ResponseEntity(new ResponseSet(errorClass), HttpStatus.NOT_FOUND);
        }
    }

}
