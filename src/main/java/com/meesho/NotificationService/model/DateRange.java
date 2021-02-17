package com.meesho.NotificationService.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateRange {

    private Date from;
    private Date to;

    @JsonProperty("page_number")
    private int pageNumber;
    private int size;
}
