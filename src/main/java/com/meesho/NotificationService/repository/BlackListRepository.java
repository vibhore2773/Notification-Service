package com.meesho.NotificationService.repository;

import com.meesho.NotificationService.model.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackListRepository {

    @Autowired
    private RedisTemplate<String,Blacklist> redisTemplate;


    public void save(String id){
        Blacklist blacklist = new Blacklist();
        blacklist.setId(id);
        redisTemplate.opsForSet().add("data",blacklist);
    }
    public List<Blacklist> findAll(){
        return (List<Blacklist>) redisTemplate.opsForSet().members("data");
    }
    public boolean exists(String id){
        Blacklist blacklist = new Blacklist();
        blacklist.setId(id);
        return redisTemplate.opsForSet().isMember("data",blacklist);
    }
    public long delete(Blacklist id){
        return redisTemplate.opsForSet().remove("data",id);
    }
}
