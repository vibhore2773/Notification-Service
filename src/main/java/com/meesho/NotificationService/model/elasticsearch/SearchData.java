
package com.meesho.NotificationService.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document( indexName = "sms_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchData implements Serializable {
    @Id
    private String id;

    @JsonProperty("phone_number")
    @Field(name = "phone_number",type = FieldType.Text)
    private String phoneNumber;

    @Field(type = FieldType.Text)
    private String message;

    @JsonProperty("created_at")
    @Field(name = "created_at", type = FieldType.Date)
    private Date createdAt;
}
