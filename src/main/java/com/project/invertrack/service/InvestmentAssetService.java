package com.project.invertrack.service;

import com.project.invertrack.model.InvestmentAsset;
import com.project.invertrack.repository.InvestmentAssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestmentAssetService {

    private final InvestmentAssetRepository assetRepository;

    public InvestmentAssetService(InvestmentAssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<InvestmentAsset> findAll() {
        return assetRepository.findAll();
    }

    public Optional<InvestmentAsset> findById(Long id) {
        return assetRepository.findById(id);
    }

    public Optional<InvestmentAsset> findBySymbol(String symbol) {
        return assetRepository.findBySymbol(symbol);
    }
} 