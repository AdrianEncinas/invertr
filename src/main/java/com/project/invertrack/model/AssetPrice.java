package com.project.invertrack.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "asset_prices")
public class AssetPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private InvestmentAsset asset;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;
} 