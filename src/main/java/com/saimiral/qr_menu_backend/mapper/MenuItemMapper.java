package com.saimiral.qr_menu_backend.mapper;

import com.saimiral.qr_menu_backend.dto.MenuItemDTO;
import com.saimiral.qr_menu_backend.entity.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItemDTO toDTO(MenuItem menuItem);
}