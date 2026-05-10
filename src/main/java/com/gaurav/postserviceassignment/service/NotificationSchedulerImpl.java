package com.gaurav.postserviceassignment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationSchedulerImpl implements NoticicationScheduler{

    private final StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;
    @Override

    @Scheduled(fixedRate = 200000)
    public void scheduleNoticication() {
        Set<String> keys =
                stringRedisTemplate.keys("user:*:pending_notifications");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            while (true) {

                String message =
                        stringRedisTemplate.opsForList()
                                .leftPop(key);

                if (message == null) {
                    break;
                }

                log.info(
                        "Batched notification processed: {}",
                        message
                );
            }
        }

    }
}

