package com.bridge2solutions.service.k8s.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<Toast> getToast() {
        final String value = "From " + id + " (version " + version + "): " + toast;
        return sessionClient.getSession("1")
            .map(sessionData -> {
                final Toast toast = new Toast();
                toast.setValue(value);
                toast.setSession(sessionData);
                return toast;
            });
    }
}
