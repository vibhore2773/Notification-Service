package com.meesho.NotificationService.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data @Component @Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataClass {
    private String request_id;

    private String phone_number;

    private String message;

    private String status;

    private String failure_code;

    private String comments;

    private Date created_at;

    private Date updated_at;

    public DataClass(){}

}
