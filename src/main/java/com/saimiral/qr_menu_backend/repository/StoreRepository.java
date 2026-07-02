package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findBySlug(String slug);
}
