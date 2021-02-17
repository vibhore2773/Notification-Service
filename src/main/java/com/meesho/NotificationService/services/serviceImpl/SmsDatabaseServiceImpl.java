package com.meesho.NotificationService.services.serviceImpl;

import com.meesho.NotificationService.model.SmsRequests;
import com.meesho.NotificationService.repository.SmsRepository;
import com.meesho.NotificationService.services.SmsDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsDatabaseServiceImpl implements SmsDatabaseService {

    @Autowired
    SmsRepository repository;

    public void save(SmsRequests sms){
        repository.save(sms);
    }

    public SmsRequests findById(int id) {
        return repository.findById(id).orElse(null);
    }
}
