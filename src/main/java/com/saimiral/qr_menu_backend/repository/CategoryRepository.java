package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStoreIdOrderBySortOrderAsc(Long storeId);
}
