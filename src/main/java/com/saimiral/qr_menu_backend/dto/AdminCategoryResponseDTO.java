package com.saimiral.qr_menu_backend.dto;

import java.util.List;

public record AdminCategoryResponseDTO(
        Long id,
        String name,
        Integer sortOrder,
        List<AdminMenuItemResponseDTO> items
) {}