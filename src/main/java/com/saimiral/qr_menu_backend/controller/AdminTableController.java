package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.TableCreateDTO;
import com.saimiral.qr_menu_backend.dto.TableResponseDTO;
import com.saimiral.qr_menu_backend.service.TableAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tables")
@RequiredArgsConstructor
public class AdminTableController {

    private final TableAdminService tableAdminService;

    @GetMapping
    public ResponseEntity<List<TableResponseDTO>> getTables(Authentication authentication) {
        return ResponseEntity.ok(tableAdminService.getTables(authentication));
    }

    @PostMapping
    public ResponseEntity<TableResponseDTO> createTable(
            @RequestBody TableCreateDTO request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tableAdminService.createTable(request, authentication));
    }

    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> deleteTable(
            @PathVariable Long tableId,
            Authentication authentication) {
        tableAdminService.deleteTable(tableId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/toggle-active")
    public ResponseEntity<TableResponseDTO> toggleActive(
            @PathVariable Long tableId,
            Authentication authentication) {
        return ResponseEntity.ok(tableAdminService.toggleActive(tableId, authentication));
    }
}