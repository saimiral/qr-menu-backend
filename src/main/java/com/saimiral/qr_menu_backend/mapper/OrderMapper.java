package com.saimiral.qr_menu_backend.mapper;

import com.saimiral.qr_menu_backend.dto.OrderItemResponseDTO;
import com.saimiral.qr_menu_backend.dto.OrderResponseDTO;
import com.saimiral.qr_menu_backend.entity.Order;
import com.saimiral.qr_menu_backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "tableNumber", source = "table.tableNumber")
    @Mapping(target = "createdAt", source = "createdAt")
    OrderResponseDTO toDTO(Order order);

    @Mapping(target = "menuItemName", source = "menuItem.name")
    @Mapping(target = "price", source = "menuItem.price")
    OrderItemResponseDTO orderItemToDTO(OrderItem orderItem);
}