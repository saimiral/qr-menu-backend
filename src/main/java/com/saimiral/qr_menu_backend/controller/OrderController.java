package com.saimiral.qr_menu_backend.controller;

import com.saimiral.qr_menu_backend.dto.OrderRequestDTO;
import com.saimiral.qr_menu_backend.dto.OrderResponseDTO;
import com.saimiral.qr_menu_backend.dto.OrderStatusUpdateDTO;
import com.saimiral.qr_menu_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Ο πελάτης στέλνει παραγγελία
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    // Η κουζίνα παίρνει τις ενεργές παραγγελίες
    @GetMapping("/kitchen/{storeSlug}")
    public ResponseEntity<List<OrderResponseDTO>> getActiveOrders(@PathVariable String storeSlug) {
        return ResponseEntity.ok(orderService.getActiveOrders(storeSlug));
    }

    // Η κουζίνα αλλάζει status παραγγελίας
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDTO request) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, request));
    }
}