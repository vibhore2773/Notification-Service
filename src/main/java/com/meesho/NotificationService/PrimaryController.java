package com.meesho.NotificationService;
import com.meesho.NotificationService.model.*;
import com.meesho.NotificationService.model.apiSmsModel.ApiSmsModel;
import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.repository.BlackListRepository;
import com.meesho.NotificationService.repository.SearchRepository;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.model.response.DataClass;
import com.meesho.NotificationService.model.response.ErrorClass;
import com.meesho.NotificationService.model.response.ResponseSet;
import com.meesho.NotificationService.services.ImiConnectClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PrimaryController {
    @Autowired
    SmsRepository repo;
    @Autowired
    KafkaController kafkaController;
    @Autowired
    BlackListRepository blackListRepository;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    ImiConnectClient apiClient;
    @PostMapping("v1/sms/send")
    public ResponseEntity<ResponseSet> send(@RequestBody Message message){
        try{
            SmsRequests sms = new SmsRequests();
            sms.setPhone_number(message.getPhone_number());
            sms.setMessage(message.getMessage());
            sms.setStatus("OK");
            repo.save(sms);
            kafkaController.sendToKafka(String.valueOf(sms.getId()));
            DataClass dataClass = DataClass.builder().request_id(String.valueOf(sms.getId())).comments("Successfully Sent").build();
            return new ResponseEntity<>(new ResponseSet(dataClass), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(new ResponseSet(new ErrorClass()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ApiSmsModel temp(@PathVariable int id){
        SmsRequests sms = repo.findById(id).orElse(null);
        return new ApiSmsModel(sms);
    }

    @GetMapping("/v1/sms/{request_id}")
    public ResponseEntity<ResponseSet> getDetails(@PathVariable("request_id") String id) {
        SmsRequests sms = repo.findById(Integer.parseInt(id)).orElse(null);
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
    public Page<SearchData> findMessage(@RequestBody Message message, @PathVariable("page_number") int page_number, @PathVariable("size") int size){

        Pageable pageable = PageRequest.of(page_number,size);
        System.out.println(searchRepository + "Search");

        Page<SearchData> data = searchRepository.findByMessageContains(message.getMessage(), pageable);
        return data;
    }


}
