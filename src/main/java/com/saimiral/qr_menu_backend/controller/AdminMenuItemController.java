package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.AdminMenuItemResponseDTO;
import com.saimiral.qr_menu_backend.dto.MenuItemRequestDTO;
import com.saimiral.qr_menu_backend.service.MenuItemAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/menu-items")
@RequiredArgsConstructor
public class AdminMenuItemController {

    private final MenuItemAdminService menuItemAdminService;

    @PostMapping
    public ResponseEntity<AdminMenuItemResponseDTO> createMenuItem(
            @RequestBody MenuItemRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuItemAdminService.createMenuItem(request, authentication));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<AdminMenuItemResponseDTO> updateMenuItem(
            @PathVariable Long itemId,
            @RequestBody MenuItemRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(menuItemAdminService.updateMenuItem(itemId, request, authentication));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long itemId,
            Authentication authentication) {
        menuItemAdminService.deleteMenuItem(itemId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{itemId}/availability")
    public ResponseEntity<AdminMenuItemResponseDTO> toggleAvailability(
            @PathVariable Long itemId,
            Authentication authentication) {
        return ResponseEntity.ok(menuItemAdminService.toggleAvailability(itemId, authentication));
    }
}