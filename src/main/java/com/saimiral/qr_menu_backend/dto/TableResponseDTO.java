package com.saimiral.qr_menu_backend.dto;

public record TableResponseDTO(
        Long id,
        Integer tableNumber,
        String qrToken,
        boolean active
) {}