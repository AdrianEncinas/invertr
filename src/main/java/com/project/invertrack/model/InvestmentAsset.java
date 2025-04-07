package com.project.invertrack.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "investment_assets")
public class InvestmentAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(nullable = false, length = 50)
    private String type;
} 