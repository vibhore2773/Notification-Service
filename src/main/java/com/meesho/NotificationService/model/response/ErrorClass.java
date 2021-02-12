package com.meesho.NotificationService.model.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ErrorClass {
    private String code;
    private String message;
    public ErrorClass(){
        code = "INVALID_REQUEST";
        message = "phone_number is mandatory";
    }
}
