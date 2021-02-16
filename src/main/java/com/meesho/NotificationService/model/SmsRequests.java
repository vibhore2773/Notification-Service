package com.meesho.NotificationService.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "sms_requests")
public class SmsRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String phone_number;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status;

    private String failure_code;

    private String failure_comments;

    @Column(nullable = false)
    private Date created_at=new Date();

    private Date updated_at;

}
