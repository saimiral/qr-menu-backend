package com.saimiral.qr_menu_backend.dto;

import com.saimiral.qr_menu_backend.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Integer tableNumber,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {}