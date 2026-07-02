package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
