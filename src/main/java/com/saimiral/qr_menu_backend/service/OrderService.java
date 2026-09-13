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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository storeTableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    private final SSE_Service sseService;
    private static final int MAX_ITEMS_PER_ORDER = 30;
    private static final int MAX_QUANTITY_PER_ITEM = 20;
    private static final Duration ORDER_COOLDOWN = Duration.ofSeconds(10);
    private final Map<Long, Instant> lastOrderTimestamps = new ConcurrentHashMap<>();

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        validateOrderRequest(request);

        TableOfQR table = storeTableRepository.findByQrToken(request.qrToken())
                .orElseThrow(() -> new RuntimeException("Invalid QR token"));

        checkRateLimit(table.getId());

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
        OrderResponseDTO response = orderMapper.toDTO(saved);

        // Push real-time event στο kitchen dashboard
        String storeSlug = table.getStore().getSlug();
        sseService.pushNewOrder(storeSlug, response);

        return response;
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

    private void validateOrderRequest(OrderRequestDTO request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new RuntimeException("Η παραγγελία πρέπει να έχει τουλάχιστον ένα προϊόν.");
        }
        if (request.items().size() > MAX_ITEMS_PER_ORDER) {
            throw new RuntimeException("Μέγιστος αριθμός διαφορετικών προϊόντων ανά παραγγελία: " + MAX_ITEMS_PER_ORDER);
        }
        for (var item : request.items()) {
            if (item.quantity() <= 0) {
                throw new RuntimeException("Η ποσότητα πρέπει να είναι τουλάχιστον 1.");
            }
            if (item.quantity() > MAX_QUANTITY_PER_ITEM) {
                throw new RuntimeException("Μέγιστη ποσότητα ανά προϊόν: " + MAX_QUANTITY_PER_ITEM);
            }
        }
    }

    private void checkRateLimit(Long tableId) {
        Instant now = Instant.now();
        Instant previous = lastOrderTimestamps.put(tableId, now);
        if (previous != null && Duration.between(previous, now).compareTo(ORDER_COOLDOWN) < 0) {
            lastOrderTimestamps.put(tableId, previous); // rollback, η παραγγελία απορρίπτεται
            throw new RuntimeException("Παρακαλώ περίμενε λίγα δευτερόλεπτα πριν στείλεις νέα παραγγελία.");
        }
    }
}