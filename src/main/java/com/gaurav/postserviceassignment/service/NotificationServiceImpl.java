package com.gaurav.postserviceassignment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void handleBotInteraction( String Content, Long userId) {

        String cooldownKey =
                "notif:cooldown:user:" + userId;

        Boolean firstInteraction =
                stringRedisTemplate.opsForValue()
                        .setIfAbsent(
                                cooldownKey,
                                "ACTIVE",
                                Duration.ofMinutes(15)
                        );
        if(Boolean.TRUE.equals(firstInteraction)){
            log.info(
                    "Instant notification sent to user {} : {}",
                    userId,
                    Content
            );
           return;
        }

            String listKey =
                    "user:" + userId
                            + ":pending_notifications";

            stringRedisTemplate.opsForList()
                    .rightPush(listKey, Content);

        }


    }


