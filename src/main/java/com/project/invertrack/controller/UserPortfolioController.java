package com.project.invertrack.controller;

import com.project.invertrack.model.UserPortfolio;
import com.project.invertrack.service.UserPortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class UserPortfolioController {

    private final UserPortfolioService portfolioService;

    public UserPortfolioController(UserPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPortfolio>> getPortfoliosByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPortfolio> getPortfolioById(@PathVariable Long id) {
        return portfolioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 