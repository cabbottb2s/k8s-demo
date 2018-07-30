package com.bridge2solutions.service.k8s.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/toast")
public class ToastController {

    private final int id = new Random(System.currentTimeMillis()).nextInt(10000);
    private final String version = ToastController.class.getPackage().getImplementationVersion();

    @Value("${toast:awkward silence}")
    private String toast;

    @Autowired
    private SessionClient sessionClient;

    @GetMapping(produces = "application/json")
    public Mono<Toast> getToast(@RequestHeader(name = "sessionId", required = false) final String rawSessionId) {

        final String sessionId = Optional.ofNullable(rawSessionId).orElse("1");
        return sessionClient.getSession(sessionId)
            .map(sessionData -> {
                final Toast fullToast = new Toast();
                fullToast.setInstanceId(id);
                fullToast.setVersion(version);
                fullToast.setValue(toast);
                fullToast.setSession(sessionData);
                return fullToast;
            });
    }
}
