package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.redis.Blacklist;
import com.meesho.NotificationService.repository.BlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private BlackListRepository repository;


    public void save(String number) {
        repository.save(number);
    }

    public Set<String> findAll() {
        return repository.findAll();
    }

    public boolean exists(String number) {
        return repository.exists(number);
    }

    public String deleteAll() {
        return String.valueOf(repository.deleteAll());
    }

    public void delete(String number) {
        repository.delete(number);
    }
}
