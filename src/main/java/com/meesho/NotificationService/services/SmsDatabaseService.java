package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.SmsRequests;
import com.meesho.NotificationService.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsDatabaseService {

    @Autowired
    SmsRepository repository;

    public void save(SmsRequests sms){
        repository.save(sms);
    }

    public SmsRequests findById(int id) {
        return repository.findById(id).orElse(null);
    }
}
