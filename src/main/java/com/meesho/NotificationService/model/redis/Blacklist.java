package com.meesho.NotificationService.model.redis;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

@Data
@Component
public class Blacklist {

    private Set<String> data;
}
