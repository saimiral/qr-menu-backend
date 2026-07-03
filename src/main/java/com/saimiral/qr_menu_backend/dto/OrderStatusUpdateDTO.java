package com.saimiral.qr_menu_backend.dto;

import com.saimiral.qr_menu_backend.entity.OrderStatus;

public record OrderStatusUpdateDTO(
        OrderStatus status
) {}