package com.project.invertrack.repository;

import com.project.invertrack.model.InvestmentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentAssetRepository extends JpaRepository<InvestmentAsset, Long> {
    Optional<InvestmentAsset> findBySymbol(String symbol);
} 