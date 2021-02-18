package com.meesho.NotificationService.services;

import org.springframework.stereotype.Service;

@Service
public interface Consumer {
    public void consume(String id) throws Exception;
}
