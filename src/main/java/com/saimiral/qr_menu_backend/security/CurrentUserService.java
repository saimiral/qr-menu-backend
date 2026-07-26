package com.saimiral.qr_menu_backend.security;

import com.saimiral.qr_menu_backend.entity.Store;
import com.saimiral.qr_menu_backend.entity.StoreUser;
import com.saimiral.qr_menu_backend.repository.StoreUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final StoreUserRepository storeUserRepository;

    public Store getCurrentStore(Authentication authentication) {
        StoreUser storeUser = storeUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));
        return storeUser.getStore();
    }
}