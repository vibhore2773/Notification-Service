package com.meesho.NotificationService.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class Blacklist implements Serializable {
    private String id;
}
