package com.saimiral.qr_menu_backend.mapper;

import com.saimiral.qr_menu_backend.dto.CategoryDTO;
import com.saimiral.qr_menu_backend.dto.MenuItemDTO;
import com.saimiral.qr_menu_backend.entity.Category;
import com.saimiral.qr_menu_backend.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "items", source = "items")
    CategoryDTO toDTO(Category category, List<MenuItemDTO> items);

    MenuItemDTO menuItemToDTO(MenuItem menuItem);
}