package com.meesho.NotificationService;


import com.meesho.NotificationService.model.PhoneNumber;
import com.meesho.NotificationService.model.redis.Blacklist;
import com.meesho.NotificationService.model.response.DataClass;
import com.meesho.NotificationService.model.response.ResponseSet;
import com.meesho.NotificationService.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class RedisController {

    @Autowired
    RedisService redisService;
    @PostMapping("/v1/blacklist")
    public ResponseEntity<ResponseSet> addToBlacklist(@RequestBody PhoneNumber phoneNumber){
        try{
            for(String number : phoneNumber.getPhone_numbers()){
                redisService.save(number);
            }
            DataClass dataClass = new DataClass();
            dataClass.setMessage("Successfully Blacklisted");
            return new ResponseEntity(new ResponseSet(dataClass), HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity(new ResponseSet(new DataClass()),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/v1/blacklist")
    public ResponseEntity<Blacklist> getBlacklist(){
        Set<String> data = redisService.findAll();
        Blacklist list = new Blacklist();
        list.setData(data);

        return new ResponseEntity(list, HttpStatus.OK);
    }
    @GetMapping("/exists/{number}")
    public String exists(@PathVariable("number") String number){
        boolean check = redisService.exists(number);
        return String.valueOf(check);
    }

    @GetMapping("v1/deleteAll")
    public String deleteAll(){
        return String.valueOf(redisService.deleteAll());

    }
    @DeleteMapping("v1/blacklist/delete/")
    public String delete(@RequestBody String phoneNumber){
        redisService.delete(phoneNumber);
        return "Success";
    }
}
