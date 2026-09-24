package com.saimiral.qr_menu_backend.config;

import com.saimiral.qr_menu_backend.entity.*;
import com.saimiral.qr_menu_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final StoreRepository storeRepository;
    private final TableRepository storeTableRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final StoreUserRepository StoreUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    @Profile("dev")
    public CommandLineRunner seedData() {
        return args -> {
            if (storeRepository.count() > 0) {
                log.info("Database already seeded, skipping...");
                return;
            }

            // Store
            Store store = new Store();
            store.setName("Καφετέρια Δοκιμή");
            store.setSlug("kafeteria-dokimi");
            store.setOwnerEmail("owner@test.com");
            storeRepository.save(store);

            // Tables
            for (int i = 1; i <= 5; i++) {
                TableOfQR table = new TableOfQR();
                table.setStore(store);
                table.setTableNumber(i);
                TableOfQR saved = storeTableRepository.save(table);
                log.info("Table {} QR Token: {}", i, saved.getQrToken());
            }

            // Category: Καφέδες
            Category coffees = new Category();
            coffees.setStore(store);
            coffees.setName("Καφέδες");
            coffees.setSortOrder(1);
            categoryRepository.save(coffees);

            MenuItem espresso = new MenuItem();
            espresso.setCategory(coffees);
            espresso.setName("Espresso");
            espresso.setDescription("Κλασικό espresso single shot");
            espresso.setPrice(new BigDecimal("1.80"));
            menuItemRepository.save(espresso);

            MenuItem freddo = new MenuItem();
            freddo.setCategory(coffees);
            freddo.setName("Freddo Espresso");
            freddo.setDescription("Παγωμένο espresso με αφρό");
            freddo.setPrice(new BigDecimal("2.50"));
            menuItemRepository.save(freddo);

            MenuItem cappuccino = new MenuItem();
            cappuccino.setCategory(coffees);
            cappuccino.setName("Cappuccino");
            cappuccino.setDescription("Espresso με ατμισμένο γάλα");
            cappuccino.setPrice(new BigDecimal("2.80"));
            menuItemRepository.save(cappuccino);

            // Category: Αναψυκτικά
            Category drinks = new Category();
            drinks.setStore(store);
            drinks.setName("Αναψυκτικά");
            drinks.setSortOrder(2);
            categoryRepository.save(drinks);

            MenuItem cola = new MenuItem();
            cola.setCategory(drinks);
            cola.setName("Coca Cola");
            cola.setDescription("330ml κουτάκι");
            cola.setPrice(new BigDecimal("2.00"));
            menuItemRepository.save(cola);

            MenuItem water = new MenuItem();
            water.setCategory(drinks);
            water.setName("Νερό");
            water.setDescription("Εμφιαλωμένο 500ml");
            water.setPrice(new BigDecimal("0.50"));
            menuItemRepository.save(water);

            // Category: Εδέσματα
            Category food = new Category();
            food.setStore(store);
            food.setName("Εδέσματα");
            food.setSortOrder(3);
            categoryRepository.save(food);

            MenuItem toast = new MenuItem();
            toast.setCategory(food);
            toast.setName("Toast");
            toast.setDescription("Τυρί, ζαμπόν, ντομάτα");
            toast.setPrice(new BigDecimal("3.50"));
            menuItemRepository.save(toast);

            MenuItem croissant = new MenuItem();
            croissant.setCategory(food);
            croissant.setName("Κρουασάν βουτύρου");
            croissant.setDescription("Φρέσκο κρουασάν με βούτυρο");
            croissant.setPrice(new BigDecimal("2.20"));
            menuItemRepository.save(croissant);

            StoreUser admin = new StoreUser();
            admin.setStore(store);
            admin.setEmail("admin@kafeteria.gr");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            StoreUserRepository.save(admin);

            StoreUser kitchen = new StoreUser();
            kitchen.setStore(store);
            kitchen.setEmail("kitchen@kafeteria.gr");
            kitchen.setPassword(passwordEncoder.encode("kitchen123"));
            kitchen.setRole(UserRole.KITCHEN);
            StoreUserRepository.save(kitchen);

            log.info("Admin: admin@kafeteria.gr / admin123");
            log.info("Kitchen: kitchen@kafeteria.gr / kitchen123");

            Store store2 = new Store();
            store2.setName("Ταβέρνα Δοκιμή");
            store2.setSlug("taverna-dokimi");
            store2.setOwnerEmail("owner2@test.com");
            storeRepository.save(store2);

            for (int i = 1; i <= 3; i++) {
                TableOfQR table2 = new TableOfQR();
                table2.setStore(store2);
                table2.setTableNumber(i);
                TableOfQR saved2 = storeTableRepository.save(table2);
                log.info("[Store2] Table {} QR Token: {}", i, saved2.getQrToken());
            }

            Category mains = new Category();
            mains.setStore(store2);
            mains.setName("Κυρίως Πιάτα");
            mains.setSortOrder(1);
            categoryRepository.save(mains);

            MenuItem souvlaki = new MenuItem();
            souvlaki.setCategory(mains);
            souvlaki.setName("Σουβλάκι Χοιρινό");
            souvlaki.setDescription("Με πίτα, ντομάτα, κρεμμύδι");
            souvlaki.setPrice(new BigDecimal("3.50"));
            menuItemRepository.save(souvlaki);

            MenuItem salad = new MenuItem();
            salad.setCategory(mains);
            salad.setName("Χωριάτικη Σαλάτα");
            salad.setDescription("Ντομάτα, αγγούρι, φέτα, ελιές");
            salad.setPrice(new BigDecimal("6.00"));
            menuItemRepository.save(salad);

            StoreUser admin2 = new StoreUser();
            admin2.setStore(store2);
            admin2.setEmail("admin@taverna.gr");
            admin2.setPassword(passwordEncoder.encode("admin123"));
            admin2.setRole(UserRole.ADMIN);
            StoreUserRepository.save(admin2);

            log.info("Admin2: admin@taverna.gr / admin123");

            log.info("✅ Test data seeded successfully!");
            log.info("Store slug: kafeteria-dokimi");
            log.info("Store2 slug: taverna-dokimi");
            log.info("Check logs above for QR tokens of each table");
        };
    }
}