package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.AdminCategoryResponseDTO;
import com.saimiral.qr_menu_backend.dto.CategoryRequestDTO;
import com.saimiral.qr_menu_backend.service.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryAdminService categoryAdminService;

    @GetMapping
    public ResponseEntity<List<AdminCategoryResponseDTO>> getMenu(Authentication authentication) {
        return ResponseEntity.ok(categoryAdminService.getMenu(authentication));
    }

    @PostMapping
    public ResponseEntity<AdminCategoryResponseDTO> createCategory(
            @RequestBody CategoryRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryAdminService.createCategory(request, authentication));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<AdminCategoryResponseDTO> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(categoryAdminService.updateCategory(categoryId, request, authentication));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            Authentication authentication) {
        categoryAdminService.deleteCategory(categoryId, authentication);
        return ResponseEntity.noContent().build();
    }
}