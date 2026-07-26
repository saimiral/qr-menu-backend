package com.saimiral.qr_menu_backend.dto;

public record StoreResponseDTO(
        Long id,
        String name,
        String slug,
        String ownerEmail
) {}