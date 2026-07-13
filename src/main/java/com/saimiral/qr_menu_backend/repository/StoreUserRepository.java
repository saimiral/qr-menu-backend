package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreUserRepository extends JpaRepository<StoreUser, Long> {
    Optional<StoreUser> findByEmail(String email);
}