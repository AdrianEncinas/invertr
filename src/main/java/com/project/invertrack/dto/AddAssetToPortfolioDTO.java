package com.project.invertrack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddAssetToPortfolioDTO {
    private Long userId;
    private Long assetId;
    private BigDecimal quantity;
    private BigDecimal avgBuyPrice;
} 