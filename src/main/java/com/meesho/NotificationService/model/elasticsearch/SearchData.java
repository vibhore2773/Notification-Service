
package com.meesho.NotificationService.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Document( indexName = "sms_details")
public class SearchData implements Serializable {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String phone_number;
    @Field(type = FieldType.Text)
    private String message;
    @Field(type = FieldType.Date)
    private Date created_at;
}
