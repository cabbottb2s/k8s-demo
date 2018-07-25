package com.bridge2solutions.service.k8s.gateway;

public class Toast {
    private String value = "";
    private String session = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
