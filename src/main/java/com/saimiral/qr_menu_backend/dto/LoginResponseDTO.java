package com.saimiral.qr_menu_backend.dto;

public record LoginResponseDTO(
        String token,
        String role,
        String storeSlug
) {}