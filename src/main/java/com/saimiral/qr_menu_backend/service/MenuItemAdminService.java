package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.AdminMenuItemResponseDTO;
import com.saimiral.qr_menu_backend.dto.MenuItemRequestDTO;
import com.saimiral.qr_menu_backend.entity.Category;
import com.saimiral.qr_menu_backend.entity.MenuItem;
import com.saimiral.qr_menu_backend.entity.Store;
import com.saimiral.qr_menu_backend.repository.CategoryRepository;
import com.saimiral.qr_menu_backend.repository.MenuItemRepository;
import com.saimiral.qr_menu_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemAdminService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public AdminMenuItemResponseDTO createMenuItem(MenuItemRequestDTO request, Authentication authentication) {
        Category category = getOwnedCategory(request.categoryId(), authentication);

        MenuItem item = new MenuItem();
        item.setCategory(category);
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setImageUrl(request.imageUrl());
        item.setAvailable(request.available() != null ? request.available() : true);

        MenuItem saved = menuItemRepository.save(item);
        return toDTO(saved);
    }

    public AdminMenuItemResponseDTO updateMenuItem(Long itemId, MenuItemRequestDTO request, Authentication authentication) {
        MenuItem item = getOwnedMenuItem(itemId, authentication);

        if (request.categoryId() != null && !request.categoryId().equals(item.getCategory().getId())) {
            Category newCategory = getOwnedCategory(request.categoryId(), authentication);
            item.setCategory(newCategory);
        }

        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setImageUrl(request.imageUrl());
        if (request.available() != null) {
            item.setAvailable(request.available());
        }

        MenuItem saved = menuItemRepository.save(item);
        return toDTO(saved);
    }

    public void deleteMenuItem(Long itemId, Authentication authentication) {
        MenuItem item = getOwnedMenuItem(itemId, authentication);
        menuItemRepository.delete(item);
    }

    public AdminMenuItemResponseDTO toggleAvailability(Long itemId, Authentication authentication) {
        MenuItem item = getOwnedMenuItem(itemId, authentication);
        item.setAvailable(!item.isAvailable());
        MenuItem saved = menuItemRepository.save(item);
        return toDTO(saved);
    }

    private Category getOwnedCategory(Long categoryId, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Η κατηγορία δεν βρέθηκε"));

        if (!category.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Δεν έχεις πρόσβαση σε αυτή την κατηγορία");
        }

        return category;
    }

    private MenuItem getOwnedMenuItem(Long itemId, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Το προϊόν δεν βρέθηκε"));

        if (!item.getCategory().getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Δεν έχεις πρόσβαση σε αυτό το προϊόν");
        }

        return item;
    }

    private AdminMenuItemResponseDTO toDTO(MenuItem item) {
        return new AdminMenuItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl(),
                item.isAvailable(),
                item.getCategory().getId()
        );
    }
}