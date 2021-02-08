package com.meesho.NotificationService.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@Component
public class PhoneNumber {

    private ArrayList<String> phone_numbers;
    PhoneNumber(){}
}
