package com.saimiral.qr_menu_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SSE_Service {

    // Όταν ξεπεραστεί, κλείνει η παλαιότερη σύνδεση
    private static final int MAX_EMITTERS_PER_STORE = 5;

    // Λίστα emitters ανά store slug
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String storeSlug) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            removeEmitter(storeSlug, emitter);
            log.info("SSE connection closed for store: {}", storeSlug);
        });

        emitter.onTimeout(() -> {
            removeEmitter(storeSlug, emitter);
            log.info("SSE connection timed out for store: {}", storeSlug);
        });

        emitter.onError(e -> {
            removeEmitter(storeSlug, emitter);
            log.warn("SSE error for store {}: {}", storeSlug, e.getMessage());
        });

        List<SseEmitter> storeEmitters =
                emitters.computeIfAbsent(storeSlug, k -> new CopyOnWriteArrayList<>());

        synchronized (storeEmitters) {
            storeEmitters.add(emitter);
            while (storeEmitters.size() > MAX_EMITTERS_PER_STORE) {
                SseEmitter oldest = storeEmitters.remove(0);
                try {
                    oldest.complete();
                } catch (Exception e) {
                    log.debug("Oldest emitter for store {} was already closed", storeSlug);
                }
            }
        }

        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));
        } catch (IOException e) {
            removeEmitter(storeSlug, emitter);
        }
        log.info("SSE connection established for store: {} ({} active)",
                storeSlug, storeEmitters.size());
        return emitter;
    }

    public void pushNewOrder(String storeSlug, Object orderData) {
        List<SseEmitter> storeEmitters = emitters.get(storeSlug);
        if (storeEmitters == null || storeEmitters.isEmpty()) {
            log.info("No SSE subscriber for store: {}", storeSlug);
            return;
        }

        for (SseEmitter emitter : storeEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("new-order")
                        .data(orderData));
            } catch (IOException | IllegalStateException e) {
                removeEmitter(storeSlug, emitter);
                log.warn("Failed to send SSE event to store {}: {}", storeSlug, e.getMessage());
            }
        }
    }

    public void pushOrderUpdate(String storeSlug, Object orderData) {
        List<SseEmitter> storeEmitters = emitters.get(storeSlug);
        if (storeEmitters == null || storeEmitters.isEmpty()) {
            return;
        }

        for (SseEmitter emitter : storeEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order-updated")
                        .data(orderData));
            } catch (IOException | IllegalStateException e) {
                removeEmitter(storeSlug, emitter);
                log.warn("Failed to update SSE event to store {}: {}", storeSlug, e.getMessage());
            }
        }
    }

    private void removeEmitter(String storeSlug, SseEmitter emitter) {
        List<SseEmitter> storeEmitters = emitters.get(storeSlug);
        if (storeEmitters != null) {
            storeEmitters.remove(emitter);
        }
    }
}