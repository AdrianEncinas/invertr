package com.project.invertrack.service;

import com.project.invertrack.model.UserPortfolio;
import com.project.invertrack.repository.UserPortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPortfolioService {

    private final UserPortfolioRepository portfolioRepository;

    public UserPortfolioService(UserPortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public List<UserPortfolio> findByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Optional<UserPortfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }
} 