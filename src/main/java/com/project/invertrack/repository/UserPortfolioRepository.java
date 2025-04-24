package com.project.invertrack.repository;

import com.project.invertrack.model.User;
import com.project.invertrack.model.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {
    List<UserPortfolio> findByUser(User user);
    Optional<UserPortfolio> findByUserAndAsset(User user, com.project.invertrack.model.InvestmentAsset asset);
    List<UserPortfolio> findByUserId(Long userId);
    Optional<UserPortfolio> findByUserIdAndAssetId(Long userId, Long assetId);
} 