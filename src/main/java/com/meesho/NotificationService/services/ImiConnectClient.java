package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.SmsRequests;

public interface ImiConnectClient {
    public String sendMessage(SmsRequests sms);
}
