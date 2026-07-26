package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.AdminCategoryResponseDTO;
import com.saimiral.qr_menu_backend.dto.AdminMenuItemResponseDTO;
import com.saimiral.qr_menu_backend.dto.CategoryRequestDTO;
import com.saimiral.qr_menu_backend.entity.Category;
import com.saimiral.qr_menu_backend.entity.MenuItem;
import com.saimiral.qr_menu_backend.entity.Store;
import com.saimiral.qr_menu_backend.repository.CategoryRepository;
import com.saimiral.qr_menu_backend.repository.MenuItemRepository;
import com.saimiral.qr_menu_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final CurrentUserService currentUserService;

    public List<AdminCategoryResponseDTO> getMenu(Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        return categoryRepository.findByStoreIdOrderBySortOrderAsc(store.getId())
                .stream()
                .map(category -> toDTO(category, menuItemRepository.findByCategoryId(category.getId())))
                .toList();
    }

    public AdminCategoryResponseDTO createCategory(CategoryRequestDTO request, Authentication authentication) {
        Store store = currentUserService.getCurrentStore(authentication);

        Category category = new Category();
        category.setStore(store);
        category.setName(request.name());
        category.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);

        Category saved = categoryRepository.save(category);
        return toDTO(saved, List.of());
    }

    public AdminCategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO request, Authentication authentication) {
        Category category = getOwnedCategory(categoryId, authentication);

        category.setName(request.name());
        if (request.sortOrder() != null) {
            category.setSortOrder(request.sortOrder());
        }

        Category saved = categoryRepository.save(category);
        return toDTO(saved, menuItemRepository.findByCategoryId(saved.getId()));
    }

    public void deleteCategory(Long categoryId, Authentication authentication) {
        Category category = getOwnedCategory(categoryId, authentication);

        List<MenuItem> items = menuItemRepository.findByCategoryId(categoryId);
        if (!items.isEmpty()) {
            throw new RuntimeException("Δεν μπορείς να διαγράψεις κατηγορία που έχει προϊόντα. Διάγραψε πρώτα τα προϊόντα.");
        }

        categoryRepository.delete(category);
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

    private AdminCategoryResponseDTO toDTO(Category category, List<MenuItem> items) {
        List<AdminMenuItemResponseDTO> itemDTOs = items.stream()
                .map(this::toItemDTO)
                .toList();

        return new AdminCategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getSortOrder(),
                itemDTOs
        );
    }

    private AdminMenuItemResponseDTO toItemDTO(MenuItem item) {
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