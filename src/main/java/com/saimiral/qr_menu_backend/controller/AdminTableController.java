package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.TableCreateDTO;
import com.saimiral.qr_menu_backend.dto.TableResponseDTO;
import com.saimiral.qr_menu_backend.entity.TableOfQR;
import com.saimiral.qr_menu_backend.service.QrCodeService;
import com.saimiral.qr_menu_backend.service.TableAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Value("${app.customer-base-url}")
    private String customerBaseUrl;

    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping("/{id}/qr-code")
    public ResponseEntity<byte[]> getTableQrCode(
            @PathVariable Long id,
            @RequestParam(defaultValue = "400") int size,
            Authentication authentication) {

        // Χρησιμοποίησε το ΙΔΙΟ method που ήδη κάνεις ownership-check
        // (μέσω CurrentUserService) στα υπόλοιπα admin table endpoints
        TableOfQR table = tableAdminService.getOwnedTable(id, authentication);

        String url = customerBaseUrl + "/menu/" + table.getQrToken();
        byte[] png = qrCodeService.generateQrCodePng(url, size);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }
}