package com.meesho.NotificationService.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String phone_number;
    private String message;
    Message(){}
}
