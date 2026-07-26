package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.CurrentUserResponseDTO;
import com.saimiral.qr_menu_backend.entity.StoreUser;
import com.saimiral.qr_menu_backend.repository.StoreUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthMeController {

    private final StoreUserRepository storeUserRepository;

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDTO> getCurrentUser(Authentication authentication) {
        StoreUser user = storeUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        return ResponseEntity.ok(new CurrentUserResponseDTO(
                user.getEmail(),
                user.getRole().name(),
                user.getStore().getSlug(),
                user.getStore().getName()
        ));
    }
}