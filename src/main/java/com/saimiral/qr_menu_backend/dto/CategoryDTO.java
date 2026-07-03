package com.saimiral.qr_menu_backend.dto;

import java.util.List;

public record CategoryDTO(
        Long id,
        String name,
        List<MenuItemDTO> items
) {}