package com.bridge2solutions.service.k8s.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

@Component
public class DefaultSessionClient implements SessionClient {

    private final WebClient webClient;

    private static final ParameterizedTypeReference<Map<String, Object>> SESSION_TYPE =
        new ParameterizedTypeReference<Map<String, Object>>() {};

    public DefaultSessionClient(@Value("${session.service.baseUrl:}") final String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public Mono<Map<String, Object>> getSession(String sessionId) {
        return webClient.get().uri("/{sessionId}", sessionId).retrieve().bodyToMono(SESSION_TYPE);
    }

    @Override
    public Mono<String> setSession(String sessionId, String sessionData) {
        return webClient.put()
            .uri("/{sessionId}/{sessionData}", sessionId, sessionData)
            .retrieve()
            .bodyToMono(String.class);
    }

    @Override
    public Mono<Void> clearSession(String sessionId) {
        return webClient.delete().uri("/{sessionId}", sessionId).retrieve().bodyToMono(Void.class);
    }
}
