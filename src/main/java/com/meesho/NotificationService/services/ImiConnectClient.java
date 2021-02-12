package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.apiSmsModel.ApiSmsModel;
import com.meesho.NotificationService.model.SmsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImiConnectClient {

    @Value("${imiconnect.url}")
    private String url;
    @Value("${imiconnect.api.key}")
    private String key;

    @Autowired
    RestTemplate restTemplate;

    public String sendMessage(SmsRequests sms) {
        ApiSmsModel apiSmsModel = new ApiSmsModel(sms);
        System.out.println("key :"+ key);
        try {
            String response = restTemplate.postForObject(url,apiSmsModel,String.class);
            return response;
        }
        catch (Exception E){
            return String.valueOf(E);
        }
    }
}
