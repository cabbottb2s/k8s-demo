package com.bridge2solutions.service.k8s.gateway;

import reactor.core.publisher.Mono;

public interface SessionClient {
    Mono<String> getSession(String sessionId);
    Mono<String> setSession(String sessionId, String sessionData);
    Mono<Void> clearSession(String sessionId);
}
