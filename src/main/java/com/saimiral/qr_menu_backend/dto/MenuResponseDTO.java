package com.saimiral.qr_menu_backend.dto;

import java.util.List;

public record MenuResponseDTO(
        String storeName,
        Integer tableNumber,
        List<CategoryDTO> categories
) {}