package com.bridge2solutions.service.k8s.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequestMapping("/motd")
public class MotdController {

    private final int id = new Random(System.currentTimeMillis()).nextInt(10000);

    @Value("${motd:not set}")
    private String motd;

    @GetMapping
    public Mono<String> getMotd() {
        return Mono.just("From " + id + ": " + motd);
    }
}
