package com.gaurav.postserviceassignment.service;

import com.gaurav.postserviceassignment.entity.AuthorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViralityServiceImpl implements  ViralityService {
    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void updateViralityScore(Long postId, AuthorType authorType, InteractionType interactionType) {
        String key =
                "post:" + postId
                        + ":virality_score";

        int score = calculateScore(
                authorType,
                interactionType);

        stringRedisTemplate.opsForValue()
                .increment(key, score);
    }

    private int calculateScore(
            AuthorType authorType,
            InteractionType interactionType){

        if(authorType == AuthorType.BOT
                && interactionType
                == InteractionType.COMMENT){

            return 1;
        }

        if(authorType == AuthorType.USER
                && interactionType
                == InteractionType.LIKE){

            return 20;
        }

        if(authorType == AuthorType.USER
                && interactionType
                == InteractionType.COMMENT){

            return 50;
        }

        return 0;
    }
}
