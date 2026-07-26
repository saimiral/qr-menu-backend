package com.saimiral.qr_menu_backend.dto;

public record CurrentUserResponseDTO(
        String email,
        String role,
        String storeSlug,
        String storeName
) {}