package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.StoreResponseDTO;
import com.saimiral.qr_menu_backend.dto.StoreUpdateDTO;
import com.saimiral.qr_menu_backend.entity.Store;
import com.saimiral.qr_menu_backend.repository.StoreRepository;
import com.saimiral.qr_menu_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreAdminService {

    private final StoreRepository storeRepository;
    private final CurrentUserService currentUserService;

    public StoreResponseDTO getStore(Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);
        return toDTO(store);
    }

    public StoreResponseDTO updateStore(StoreUpdateDTO request, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);
        store.setName(request.name());
        Store saved = storeRepository.save(store);
        return toDTO(saved);
    }

    private StoreResponseDTO toDTO(Store store) {
        return new StoreResponseDTO(
                store.getId(),
                store.getName(),
                store.getSlug(),
                store.getOwnerEmail()
        );
    }
}