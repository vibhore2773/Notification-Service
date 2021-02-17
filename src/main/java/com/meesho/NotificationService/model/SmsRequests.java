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

    @Column( name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "failure_comments")
    private String failureComments;

    @Column(name = "created_at",nullable = false)
    private Date createdAt =new Date();

    @Column(name = "updated_at")
    private Date updatedAt;

}
