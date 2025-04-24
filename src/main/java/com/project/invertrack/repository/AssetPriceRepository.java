package com.project.invertrack.repository;

import com.project.invertrack.model.AssetPrice;
import com.project.invertrack.model.InvestmentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssetPriceRepository extends JpaRepository<AssetPrice, Long> {
    List<AssetPrice> findByAsset(InvestmentAsset asset);
    List<AssetPrice> findByAssetAndDateBetween(InvestmentAsset asset, LocalDateTime startDate, LocalDateTime endDate);
} 