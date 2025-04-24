package com.project.invertrack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PortfolioDTO {
    private Long id;
    private Long userId;
    private Long assetId;
    private String symbol;
    private String name;
    private BigDecimal quantity;
    private BigDecimal averagePrice;
    private BigDecimal currentPrice;
} 