package com.saimiral.qr_menu_backend.dto;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long id,
        String menuItemName,
        BigDecimal price,
        Integer quantity,
        String notes
) {}