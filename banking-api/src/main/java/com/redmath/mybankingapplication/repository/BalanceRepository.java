package com.redmath.mybankingapplication.repository;

import com.redmath.mybankingapplication.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Balance findByAccountId(long id);

    List<Balance> findAllByAccountId(long accountId);
}
