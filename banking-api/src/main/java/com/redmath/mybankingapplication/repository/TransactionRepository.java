package com.redmath.mybankingapplication.repository;

import com.redmath.mybankingapplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(long id);
}
