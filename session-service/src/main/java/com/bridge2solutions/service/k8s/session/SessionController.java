package com.bridge2solutions.service.k8s.session;

import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("sessions")
public class SessionController {

    private final SessionService service;
    private final int id = new Random(System.currentTimeMillis()).nextInt(10000);
    private final String version = SessionController.class.getPackage().getImplementationVersion();

    public SessionController(final SessionService service) {
        this.service = service;
    }

    @GetMapping("{sessionId}")
    public String getSession(@PathVariable("sessionId") final String sessionId) {
        return "From " + id + " (version " + version + "): " + service.getSession(sessionId);
    }

    @PutMapping("{sessionId}/{sessionData}")
    public String putSession(
        @PathVariable("sessionId") final String sessionId,
        @PathVariable("sessionData") final String sessionData) {

        service.setSession(sessionId, sessionData);
        return "From " + id + " (version " + version + "): OK";
    }

    @DeleteMapping("{sessionId}")
    public String deleteSession(@PathVariable("sessionId") final String sessionId) {
        service.clearSession(sessionId);
        return "From " + id + " (version " + version + "): OK";
    }
}
