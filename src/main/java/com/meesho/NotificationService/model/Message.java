package com.meesho.NotificationService.model;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@Component
public class Message {
    private String phone_number;
    private String message;
    Message(){}
}
