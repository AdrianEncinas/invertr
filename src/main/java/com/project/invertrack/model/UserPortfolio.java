package com.project.invertrack.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_portfolios")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private InvestmentAsset asset;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantity;

    @Column(name = "avg_buy_price", nullable = false, precision = 20, scale = 4)
    private BigDecimal avgBuyPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 