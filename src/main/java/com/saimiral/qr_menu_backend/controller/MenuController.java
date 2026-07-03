package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.MenuResponseDTO;
import com.saimiral.qr_menu_backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{qrToken}")
    public ResponseEntity<MenuResponseDTO> getMenu(@PathVariable String qrToken) {
        return ResponseEntity.ok(menuService.getMenu(qrToken));
    }
}