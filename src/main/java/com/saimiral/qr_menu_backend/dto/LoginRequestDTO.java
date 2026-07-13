package com.saimiral.qr_menu_backend.dto;

public record LoginRequestDTO(
        String email,
        String password
) {}