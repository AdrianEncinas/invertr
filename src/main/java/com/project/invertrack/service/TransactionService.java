package com.project.invertrack.service;

import com.project.invertrack.model.Transaction;
import com.project.invertrack.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }
} 