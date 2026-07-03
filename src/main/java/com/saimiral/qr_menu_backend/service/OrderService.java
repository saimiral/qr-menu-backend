package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.OrderRequestDTO;
import com.saimiral.qr_menu_backend.dto.OrderResponseDTO;
import com.saimiral.qr_menu_backend.dto.OrderStatusUpdateDTO;
import com.saimiral.qr_menu_backend.entity.MenuItem;
import com.saimiral.qr_menu_backend.entity.Order;
import com.saimiral.qr_menu_backend.entity.OrderItem;
import com.saimiral.qr_menu_backend.entity.OrderStatus;
import com.saimiral.qr_menu_backend.entity.TableOfQR;
import com.saimiral.qr_menu_backend.mapper.OrderMapper;
import com.saimiral.qr_menu_backend.repository.MenuItemRepository;
import com.saimiral.qr_menu_backend.repository.OrderRepository;
import com.saimiral.qr_menu_backend.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository storeTableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        TableOfQR table = storeTableRepository.findByQrToken(request.qrToken())
                .orElseThrow(() -> new RuntimeException("Invalid QR token"));

        Order order = new Order();
        order.setTable(table);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = request.items().stream()
                .map(itemRequest -> {
                    MenuItem menuItem = menuItemRepository.findById(itemRequest.menuItemId())
                            .orElseThrow(() -> new RuntimeException("MenuItem not found: " + itemRequest.menuItemId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemRequest.quantity());
                    orderItem.setNotes(itemRequest.notes());
                    return orderItem;
                })
                .toList();

        order.setItems(items);
        Order saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }

    @Transactional
    public OrderResponseDTO updateStatus(Long orderId, OrderStatusUpdateDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(request.status());
        return orderMapper.toDTO(orderRepository.save(order));
    }

    public List<OrderResponseDTO> getActiveOrders(String storeSlug) {
        return orderRepository
                .findByTableStoreSlugAndStatusNot(storeSlug, OrderStatus.PAID)
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }
}