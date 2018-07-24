package com.bridge2solutions.service.k8s.session;


import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
@CacheConfig(cacheNames = Constants.SESSIONS_CACHE)
public class DefaultSessionService implements SessionService {

    @Cacheable(key = "#sessionId")
    @Override
    public String getSession(String sessionId) {
        return "Session auto-created " + OffsetDateTime.now();
    }

    @CachePut(key = "#sessionId")
    @Override
    public String setSession(String sessionId, String sessionData) {
        return sessionData;
    }

    @CacheEvict(key = "#sessionId")
    @Override
    public void clearSession(String sessionId) {

    }
}
