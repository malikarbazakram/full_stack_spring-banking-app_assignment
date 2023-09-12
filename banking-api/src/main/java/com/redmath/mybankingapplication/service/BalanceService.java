package com.redmath.mybankingapplication.service;

import com.redmath.mybankingapplication.model.Balance;
import com.redmath.mybankingapplication.repository.AccountRepository;
import com.redmath.mybankingapplication.repository.BalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.redmath.mybankingapplication.model.Account;

import java.time.LocalDate;
import java.util.List;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    @Autowired
    private final AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(BalanceService.class);

    @Autowired
    public BalanceService(BalanceRepository balanceRepository, AccountRepository accountRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
    }

    public Balance getBalanceByAccountId(long accountId) {
        try {
            Balance balance = balanceRepository.findByAccountId(accountId);
            if (balance == null) {
                logger.error("Balance not found for Account ID: " + accountId);
                throw new RuntimeException("Balance not found for Account ID: " + accountId);
            }
            return balance;
        } catch (Exception ex) {
            logger.error("Error while retrieving balance by Account ID: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to retrieve balance by Account ID: " + accountId);
        }
    }

    public boolean updateBalance(long accountId, double amount, String transactionType) {
        try {
            Balance balanceToUpdate = getBalanceByAccountId(accountId);

            // Calculate the new balance based on the transaction type
            if (transactionType.equalsIgnoreCase("credit")) {
                balanceToUpdate.setAmount(balanceToUpdate.getAmount() + amount);
            } else if (transactionType.equalsIgnoreCase("debit")) {
                balanceToUpdate.setAmount(balanceToUpdate.getAmount() - amount);
            }

            balanceToUpdate.setDate(LocalDate.now());
            balanceToUpdate.setBalanceType(transactionType);

            balanceRepository.save(balanceToUpdate);
            return true;
        } catch (Exception ex) {
            logger.error("Error while updating balance for Account ID: " + accountId + ": " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to update balance for Account ID: " + accountId);
        }
    }

    public void createBalance(long accountId) {
        try {
            Balance newBalance = new Balance();
            newBalance.setBalanceType("New");
            newBalance.setDate(LocalDate.now());
            newBalance.setAmount(0.00);

            Account account = accountRepository.findById(accountId).orElseThrow(() -> {
                logger.error("Account not found for creating balance: Account ID - " + accountId);
                return new RuntimeException("Account not found for creating balance: Account ID - " + accountId);
            });

            newBalance.setAccount(account);

            balanceRepository.save(newBalance);
        } catch (Exception ex) {
            logger.error("Error while creating balance for Account ID: " + accountId + ": " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to create balance for Account ID: " + accountId);
        }
    }

    public List<Balance> findAllBalances(long accountId) {

        return balanceRepository.findAllByAccountId(accountId);
    }
}
