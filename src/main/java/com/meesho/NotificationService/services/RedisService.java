package com.meesho.NotificationService.services;

import java.util.Set;

public interface RedisService {

    public void save(String number);

    public Set<String> findAll();

    public boolean exists(String number);

    public String deleteAll();

    public void delete(String number);
}
