package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.StoreResponseDTO;
import com.saimiral.qr_menu_backend.dto.StoreUpdateDTO;
import com.saimiral.qr_menu_backend.service.StoreAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final StoreAdminService storeAdminService;

    @GetMapping
    public ResponseEntity<StoreResponseDTO> getStore(Authentication authentication) {
        return ResponseEntity.ok(storeAdminService.getStore(authentication));
    }

    @PutMapping
    public ResponseEntity<StoreResponseDTO> updateStore(
            @RequestBody StoreUpdateDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(storeAdminService.updateStore(request, authentication));
    }
}