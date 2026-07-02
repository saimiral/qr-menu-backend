package com.saimiral.qr_menu_backend.repository;

import com.saimiral.qr_menu_backend.entity.Order;
import com.saimiral.qr_menu_backend.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTableStoreSlugAndStatusNot(String slug, OrderStatus status);
}