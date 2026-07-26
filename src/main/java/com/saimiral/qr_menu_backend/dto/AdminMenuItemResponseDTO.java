package com.saimiral.qr_menu_backend.dto;

import java.math.BigDecimal;

public record AdminMenuItemResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        boolean available,
        Long categoryId
) {}