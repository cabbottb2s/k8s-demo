package com.bridge2solutions.service.k8s.gateway;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface SessionClient {
    Mono<Map<String, Object>> getSession(String sessionId);
    Mono<String> setSession(String sessionId, String sessionData);
    Mono<Void> clearSession(String sessionId);
}
