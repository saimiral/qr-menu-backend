package com.saimiral.qr_menu_backend.dto;

import java.util.List;

public record OrderRequestDTO(
        String qrToken,
        List<OrderItemRequestDTO> items
) {}