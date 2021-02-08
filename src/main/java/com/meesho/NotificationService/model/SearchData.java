
package com.meesho.NotificationService.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document( indexName = "sms_details")
public class SearchData {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String phone_number;
    @Field(type = FieldType.Text)
    private String message;
    @Field(type = FieldType.Date)
    private Date created_at;

    public void setData(SmsRequests sms){
        this.id= String.valueOf(sms.getId());
        this.phone_number=sms.getPhone_number();
        this.message=sms.getMessage();
        this.created_at = sms.getCreated_at();
    }

}
