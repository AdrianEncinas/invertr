package com.project.invertrack.controller;

import com.project.invertrack.dto.AddAssetToPortfolioDTO;
import com.project.invertrack.dto.PortfolioDTO;
import com.project.invertrack.model.UserPortfolio;
import com.project.invertrack.service.UserPortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolio")
public class UserPortfolioController {

    private final UserPortfolioService portfolioService;

    public UserPortfolioController(UserPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/addAsset")
    public ResponseEntity<?> addAssetToPortfolio(@RequestBody AddAssetToPortfolioDTO dto) {
        try {
            UserPortfolio portfolio = portfolioService.addAssetToPortfolio(dto);
            return ResponseEntity.ok(convertToDTO(portfolio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al añadir el asset al portfolio: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPortfoliosByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(portfolioService.findByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los portfolios: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolioById(@PathVariable Long id) {
        try {
            return portfolioService.findById(id)
                    .map(portfolio -> ResponseEntity.ok(convertToDTO(portfolio)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el portfolio: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/with-prices")
    public ResponseEntity<?> getPortfolioWithPrices(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(portfolioService.getPortfolioWithPrices(userId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el portfolio con precios: " + e.getMessage());
        }
    }

    private PortfolioDTO convertToDTO(UserPortfolio portfolio) {
        PortfolioDTO dto = new PortfolioDTO();
        dto.setId(portfolio.getId());
        dto.setUserId(portfolio.getUser().getId());
        dto.setAssetId(portfolio.getAsset().getId());
        dto.setSymbol(portfolio.getAsset().getSymbol());
        dto.setName(portfolio.getAsset().getName());
        dto.setQuantity(portfolio.getQuantity());
        dto.setAveragePrice(portfolio.getAvgBuyPrice());
        return dto;
    }
} 