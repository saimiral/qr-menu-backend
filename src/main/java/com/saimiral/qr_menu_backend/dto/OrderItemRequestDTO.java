package com.saimiral.qr_menu_backend.dto;

public record OrderItemRequestDTO(
        Long menuItemId,
        Integer quantity,
        String notes
) {}