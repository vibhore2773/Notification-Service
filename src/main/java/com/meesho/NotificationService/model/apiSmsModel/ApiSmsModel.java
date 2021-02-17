package com.meesho.NotificationService.model.apiSmsModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meesho.NotificationService.model.SmsRequests;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiSmsModel {

    @JsonProperty("deliverychannel")
    private String deliveryChannel;

    @JsonProperty("channels")
    private Channels channels;

    @JsonProperty("destination")
    List<Destination> destination;

    public ApiSmsModel(SmsRequests sms){
        this.deliveryChannel = "sms";
        this.channels = new Channels(new Sms(sms.getMessage()));
        Destination d = Destination.builder().correlationid(String.valueOf(sms.getId())).msisdn(Arrays.asList(sms.getPhoneNumber())).build();
        this.destination = Arrays.asList(d);
    }
}
