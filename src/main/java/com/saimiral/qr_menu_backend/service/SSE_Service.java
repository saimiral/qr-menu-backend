package com.saimiral.qr_menu_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SSE_Service {

    // Ένα emitter ανά store slug
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String storeSlug) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            emitters.remove(storeSlug);
            log.info("SSE connection closed for store: {}", storeSlug);
        });

        emitter.onTimeout(() -> {
            emitters.remove(storeSlug);
            log.info("SSE connection timed out for store: {}", storeSlug);
        });

        emitter.onError(e -> {
            emitters.remove(storeSlug);
            log.warn("SSE error for store {}: {}", storeSlug, e.getMessage());
        });

        emitters.put(storeSlug, emitter);
        log.info("SSE connection established for store: {}", storeSlug);
        return emitter;
    }

    public void pushNewOrder(String storeSlug, Object orderData) {
        SseEmitter emitter = emitters.get(storeSlug);
        if (emitter == null) {
            log.info("No SSE subscriber for store: {}", storeSlug);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("new-order")
                    .data(orderData));
        } catch (IOException e) {
            emitters.remove(storeSlug);
            log.warn("Failed to send SSE event to store {}: {}", storeSlug, e.getMessage());
        }
    }
}