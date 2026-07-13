package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.LoginRequestDTO;
import com.saimiral.qr_menu_backend.dto.LoginResponseDTO;
import com.saimiral.qr_menu_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}