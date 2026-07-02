package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.TableOfQR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<TableOfQR, Long> {
    Optional<TableOfQR> findByQrToken(String qrToken);
    List<TableOfQR> findByStoreId(Long storeId);
}