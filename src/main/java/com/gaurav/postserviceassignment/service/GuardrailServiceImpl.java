package com.gaurav.postserviceassignment.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class GuardrailServiceImpl implements GuardrailService {

    private final StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;

    @Override
    public void validateDepth(Integer depthlevel) {
        if (depthlevel >20) {
            throw new RuntimeException("Depth level is greater than 20");
        }
    }

    @Override
    public void validateHorizonalCap(Long postId) {
        String key="post"+postId+":bot_count";
        Long count=stringRedisTemplate.opsForValue().increment(key);
        if (count>100) {
            stringRedisTemplate.opsForValue().decrement(key);
            throw new RuntimeException("Horizonal bot limit is greater than 100");
        }
    }

    @Override
    public void validateCoolDownCaplCap(Long botId, Long humanId) {
        String key="cooldown:bot:"+botId+":human:"+humanId;
        Boolean success=stringRedisTemplate.opsForValue().setIfAbsent(
                key,"Blocked", Duration.ofMinutes(15)
        );
        if(Boolean.FALSE.equals(success)) {
            throw new RuntimeException("CoolDown active");
        }

    }


}
