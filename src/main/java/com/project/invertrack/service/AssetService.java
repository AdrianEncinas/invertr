package com.project.invertrack.service;

import com.project.invertrack.dto.AssetDTO;
import com.project.invertrack.model.InvestmentAsset;
import com.project.invertrack.repository.InvestmentAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    @Autowired
    private InvestmentAssetRepository assetRepository;

    @Transactional
    public InvestmentAsset createAsset(AssetDTO assetDTO) {
        InvestmentAsset asset = new InvestmentAsset();
        asset.setName(assetDTO.getName());
        asset.setSymbol(assetDTO.getSymbol());
        asset.setType(assetDTO.getType());
        return assetRepository.save(asset);
    }

    public List<InvestmentAsset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<InvestmentAsset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Optional<InvestmentAsset> getAssetBySymbol(String symbol) {
        return assetRepository.findBySymbol(symbol);
    }

    @Transactional
    public InvestmentAsset updateAsset(Long id, AssetDTO assetDTO) {
        return assetRepository.findById(id)
                .map(asset -> {
                    asset.setName(assetDTO.getName());
                    asset.setSymbol(assetDTO.getSymbol());
                    asset.setType(assetDTO.getType());
                    return assetRepository.save(asset);
                })
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));
    }

    @Transactional
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }
} 