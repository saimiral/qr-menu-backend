package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.CategoryDTO;
import com.saimiral.qr_menu_backend.dto.MenuItemDTO;
import com.saimiral.qr_menu_backend.dto.MenuResponseDTO;
import com.saimiral.qr_menu_backend.entity.Category;
import com.saimiral.qr_menu_backend.entity.TableOfQR;
import com.saimiral.qr_menu_backend.mapper.CategoryMapper;
import com.saimiral.qr_menu_backend.mapper.MenuItemMapper;
import com.saimiral.qr_menu_backend.repository.CategoryRepository;
import com.saimiral.qr_menu_backend.repository.MenuItemRepository;
import com.saimiral.qr_menu_backend.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final TableRepository storeTableRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final CategoryMapper categoryMapper;
    private final MenuItemMapper menuItemMapper;

    public MenuResponseDTO getMenu(String qrToken) {
        TableOfQR table = storeTableRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new RuntimeException("Invalid QR token"));

        List<Category> categories = categoryRepository.findByStoreIdOrderBySortOrderAsc(
                table.getStore().getId()
        );

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    List<MenuItemDTO> items = menuItemRepository
                            .findByCategoryIdAndAvailableTrue(category.getId())
                            .stream()
                            .map(menuItemMapper::toDTO)
                            .toList();
                    return categoryMapper.toDTO(category, items);
                })
                .toList();

        return new MenuResponseDTO(
                table.getStore().getName(),
                table.getTableNumber(),
                categoryDTOs
        );
    }
}