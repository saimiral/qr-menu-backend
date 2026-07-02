package com.saimiral.qr_menu_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "store_tables")
public class TableOfQR extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false, unique = true, updatable = false)
    private String qrToken;

    @Column(nullable = false)
    private boolean active = true;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        this.qrToken = UUID.randomUUID().toString();
    }
}