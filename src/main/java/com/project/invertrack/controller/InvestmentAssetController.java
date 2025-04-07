package com.project.invertrack.controller;

import com.project.invertrack.model.InvestmentAsset;
import com.project.invertrack.service.InvestmentAssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class InvestmentAssetController {

    private final InvestmentAssetService assetService;

    public InvestmentAssetController(InvestmentAssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<List<InvestmentAsset>> getAllAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentAsset> getAssetById(@PathVariable Long id) {
        return assetService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<InvestmentAsset> getAssetBySymbol(@PathVariable String symbol) {
        return assetService.findBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 