package com.meesho.NotificationService.repository;

import com.meesho.NotificationService.model.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class BlackListRepository {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    public void save(String number){
        redisTemplate.opsForSet().add("data",number);
    }
    public Set<String> findAll(){
        return redisTemplate.opsForSet().members("data");
    }
    public boolean exists(String number){
        return redisTemplate.opsForSet().isMember("data",number);
    }
    public long delete(String number){
        return redisTemplate.opsForSet().remove("data",number);
    }
    public boolean deleteAll(){
        return redisTemplate.delete("data");
    }
}
