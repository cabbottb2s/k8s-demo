package com.bridge2solutions.service.k8s.gateway;

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

    @Value("${toast:awkward silence}")
    private String toast;

    @GetMapping
    public Mono<String> getToast() {
        return Mono.just("From " + id + ": " + toast);
    }
}
