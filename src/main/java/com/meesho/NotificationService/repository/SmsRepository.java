package com.meesho.NotificationService.repository;

import com.meesho.NotificationService.model.Sms_requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends JpaRepository<Sms_requests,Integer> {

    @Modifying
    @Query(value = "update sms_requests set failure_comments= :comment where id= :id",nativeQuery = true)
    void updateFailureComments(@Param("id") int id,@Param("comment") String number_not_in_blackList);
}
