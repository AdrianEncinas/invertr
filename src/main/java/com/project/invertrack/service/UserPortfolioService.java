package com.project.invertrack.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.invertrack.dto.AddAssetToPortfolioDTO;
import com.project.invertrack.model.UserPortfolio;
import com.project.invertrack.repository.UserPortfolioRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPortfolioService {

    private final UserPortfolioRepository portfolioRepository;
    private final UserService userService;
    private final InvestmentAssetService assetService;
    private final PriceService priceService;

    public UserPortfolioService(UserPortfolioRepository portfolioRepository, 
                              UserService userService, 
                              InvestmentAssetService assetService,
                              PriceService priceService) {
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
        this.assetService = assetService;
        this.priceService = priceService;
    }

    public List<Map<String, Object>> findByUserId(Long userId) {
        log.info("Buscando portfolios para el usuario: {}", userId);
        List<UserPortfolio> portfolios = portfolioRepository.findByUserId(userId);
        log.info("Número de portfolios encontrados: {}", portfolios.size());
        
        return portfolios.stream()
                .map(portfolio -> {
                    log.info("Procesando portfolio: {}", portfolio.getId());
                    Map<String, Object> assetInfo = priceService.getAssetInfo(portfolio.getAsset().getSymbol());
                    Map<String, Object> portfolioInfo = Map.of(
                        "id", portfolio.getId(),
                        "symbol", portfolio.getAsset().getSymbol(),
                        "name", portfolio.getAsset().getName(),
                        "quantity", portfolio.getQuantity(),
                        "avgBuyPrice", portfolio.getAvgBuyPrice(),
                        "currentPrice", assetInfo.get("currentPrice")
                    );
                    return portfolioInfo;
                })
                .collect(Collectors.toList());
    }

    public Optional<UserPortfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    public UserPortfolio addAssetToPortfolio(AddAssetToPortfolioDTO dto) {
        // Buscar si ya existe una entrada para este usuario y asset
        Optional<UserPortfolio> existingPortfolio = portfolioRepository.findByUserIdAndAssetId(dto.getUserId(), dto.getAssetId());
        
        if (existingPortfolio.isPresent()) {
            // Si existe, actualizar la cantidad y el precio promedio
            UserPortfolio portfolio = existingPortfolio.get();
            portfolio.setQuantity(portfolio.getQuantity().add(dto.getQuantity()));
            // Calcular nuevo precio promedio ponderado
            BigDecimal totalValue = portfolio.getQuantity().multiply(portfolio.getAvgBuyPrice())
                    .add(dto.getQuantity().multiply(dto.getAvgBuyPrice()));
            BigDecimal totalQuantity = portfolio.getQuantity().add(dto.getQuantity());
            portfolio.setAvgBuyPrice(totalValue.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));
            return portfolioRepository.save(portfolio);
        } else {
            // Si no existe, crear nueva entrada
            UserPortfolio portfolio = new UserPortfolio();
            portfolio.setUser(userService.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
            portfolio.setAsset(assetService.findById(dto.getAssetId())
                    .orElseThrow(() -> new RuntimeException("Asset no encontrado")));
            portfolio.setQuantity(dto.getQuantity());
            portfolio.setAvgBuyPrice(dto.getAvgBuyPrice());
            return portfolioRepository.save(portfolio);
        }
    }

    public List<Map<String, Object>> getPortfolioWithPrices(Long userId) {
        List<UserPortfolio> portfolios = portfolioRepository.findByUserId(userId);
        return portfolios.stream()
                .map(portfolio -> {
                    Map<String, Object> assetInfo = priceService.getAssetInfo(portfolio.getAsset().getSymbol());
                    Map<String, Object> portfolioInfo = Map.of(
                        "id", portfolio.getId(),
                        "symbol", portfolio.getAsset().getSymbol(),
                        "name", portfolio.getAsset().getName(),
                        "quantity", portfolio.getQuantity(),
                        "avgBuyPrice", portfolio.getAvgBuyPrice(),
                        "currentPrice", assetInfo.get("currentPrice"),
                        "change", assetInfo.get("change"),
                        "changePercent", assetInfo.get("changePercent")
                    );
                    return portfolioInfo;
                })
                .collect(Collectors.toList());
    }
} 