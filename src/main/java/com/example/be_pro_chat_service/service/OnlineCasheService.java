package com.example.be_pro_chat_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineCasheService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String ONLINE = "online:";
    private static final Duration TTL = Duration.ofMinutes(10);

    public void markOnline(String username){
        String key = ONLINE+username;
        log.error(username + "marked as online user.");
        redisTemplate.opsForValue().set(key, "1", TTL);
    }

    public void markOffine(String username){
        log.error(username + "marked as offine user.");
        redisTemplate.delete(ONLINE+username);
    }

    public Boolean isOnline (String username){
        return Boolean.TRUE.equals(redisTemplate.hasKey(ONLINE+username));
    }

    public Set<String> getOnlineUsers(){
        Set<String> keys = redisTemplate.keys(ONLINE+"*");
        if (keys==null){
            return Set.of();
        }
        return keys.stream()
                .map(k -> k.replace(ONLINE, ""))
                .collect(Collectors.toSet());

    }
}
