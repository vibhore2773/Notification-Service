package com.meesho.NotificationService.response;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DataClass {
    private String request_id;
    private String comments;
    public DataClass(){
        request_id = "someUnique_ID";
        comments = "Successfully Sent";
    }
}
