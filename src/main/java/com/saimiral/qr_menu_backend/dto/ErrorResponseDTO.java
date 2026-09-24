package com.saimiral.qr_menu_backend.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        Instant timestamp,
        int status,
        String message
) {}