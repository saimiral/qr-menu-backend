package com.saimiral.qr_menu_backend.dto;

import java.math.BigDecimal;

public record MenuItemRequestDTO(
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Boolean available,
        Long categoryId
) {}