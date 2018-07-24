package com.bridge2solutions.service.k8s.session;

public interface SessionService {

    String getSession(String sessionId);
    String setSession(String sessionId, String sessionData);
    void clearSession(String sessionId);
}
