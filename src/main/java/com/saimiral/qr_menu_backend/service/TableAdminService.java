package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.TableCreateDTO;
import com.saimiral.qr_menu_backend.dto.TableResponseDTO;
import com.saimiral.qr_menu_backend.entity.Store;
import com.saimiral.qr_menu_backend.entity.TableOfQR;
import com.saimiral.qr_menu_backend.repository.TableRepository;
import com.saimiral.qr_menu_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableAdminService {

    private final TableRepository tableRepository;
    private final CurrentUserService currentUserService;

    public List<TableResponseDTO> getTables(Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);
        return tableRepository.findByStoreId(store.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TableResponseDTO createTable(TableCreateDTO request, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        TableOfQR table = new TableOfQR();
        table.setStore(store);
        table.setTableNumber(request.tableNumber());

        TableOfQR saved = tableRepository.save(table);
        return toDTO(saved);
    }

    public void deleteTable(Long tableId, Authentication authentication) {
        TableOfQR table = getOwnedTable(tableId, authentication);
        tableRepository.delete(table);
    }

    public TableResponseDTO toggleActive(Long tableId, Authentication authentication) {
        TableOfQR table = getOwnedTable(tableId, authentication);
        table.setActive(!table.isActive());
        TableOfQR saved = tableRepository.save(table);
        return toDTO(saved);
    }

    public TableOfQR getOwnedTable(Long tableId, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        TableOfQR table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Το τραπέζι δεν βρέθηκε"));

        if (!table.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Δεν έχεις πρόσβαση σε αυτό το τραπέζι");
        }

        return table;
    }

    private TableResponseDTO toDTO(TableOfQR table) {
        return new TableResponseDTO(
                table.getId(),
                table.getTableNumber(),
                table.getQrToken(),
                table.isActive()
        );
    }
}