package com.meesho.NotificationService;

import com.meesho.NotificationService.model.Sms_requests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms_requests,Integer> {
}
