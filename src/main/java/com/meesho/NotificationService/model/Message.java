package com.meesho.NotificationService.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Message {
    private String phone_number;
    private String message;
    Message(){}
}
