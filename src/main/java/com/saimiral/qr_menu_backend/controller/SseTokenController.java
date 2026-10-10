package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.service.SseTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SseTokenController {

    private final SseTokenService sseTokenService;

    @PostMapping("/sse-token")
    public ResponseEntity<Map<String, String>> issueSseToken(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        String ticket = sseTokenService.issue(authentication.getName(), role);
        return ResponseEntity.ok(Map.of("token", ticket));
    }
}