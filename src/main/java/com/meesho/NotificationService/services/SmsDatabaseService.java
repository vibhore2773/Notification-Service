package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.SmsRequests;

public interface SmsDatabaseService {

    public void save(SmsRequests sms);
    public SmsRequests findById(int id);
}
