package com.project.invertrack.repository;

import com.project.invertrack.model.Transaction;
import com.project.invertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndAsset(User user, com.project.invertrack.model.InvestmentAsset asset);
    List<Transaction> findByUserId(Long userId);
} 