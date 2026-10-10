package com.saimiral.qr_menu_backend.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseTokenService {

    private static final Duration TTL = Duration.ofSeconds(30);

    public record SseTicket(String email, String role, Instant expiresAt) {}

    private final Map<String, SseTicket> tickets = new ConcurrentHashMap<>();

    public String issue(String email, String role) {
        purgeExpired();
        String ticket = UUID.randomUUID().toString();
        tickets.put(ticket, new SseTicket(email, role, Instant.now().plus(TTL)));
        return ticket;
    }

    // Μιας χρήσης: το remove() το διαγράφει αμέσως
    public Optional<SseTicket> consume(String ticket) {
        SseTicket found = tickets.remove(ticket);
        if (found == null || found.expiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }
        return Optional.of(found);
    }

    private void purgeExpired() {
        Instant now = Instant.now();
        tickets.values().removeIf(t -> t.expiresAt().isBefore(now));
    }
}