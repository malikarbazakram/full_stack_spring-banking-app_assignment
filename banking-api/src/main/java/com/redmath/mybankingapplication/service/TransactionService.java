package com.redmath.mybankingapplication.service;

import com.redmath.mybankingapplication.model.Account;
import com.redmath.mybankingapplication.model.Transaction;
import com.redmath.mybankingapplication.repository.AccountRepository;
import com.redmath.mybankingapplication.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BalanceService balanceService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            BalanceService balanceService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.balanceService = balanceService;
    }

    public List<Transaction> getTransactionsByAccountId(long accountId) {
        try {
            List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
            return transactions;
        } catch (Exception ex) {
            logger.error("Error while retrieving transactions for account ID: " + accountId, ex);
            throw new RuntimeException("Unable to retrieve transactions for account ID: " + accountId);
        }
    }

    public Boolean createTransaction(Double amount, String transactionType, String description, long accountId) {
        try {
            // Find the account
            Account account = accountRepository.findById(accountId).orElse(null);
            if (account == null) {
                logger.error("Account not found for ID: " + accountId);
                throw new IllegalArgumentException("Account not found for ID: " + accountId);
            }

            // Create the transaction
            Transaction transaction = new Transaction(LocalDate.now(), amount, transactionType, description, account);
            transactionRepository.save(transaction);

            // Update the balance
            Boolean balanceUpdateStatus = balanceService.updateBalance(accountId, amount, transactionType);

            return balanceUpdateStatus;
        } catch (Exception ex) {
            logger.error("Error while creating transaction for account ID: " + accountId, ex);
            throw new RuntimeException("Unable to create transaction for account ID: " + accountId);
        }
    }

    public Boolean handleFunds(String status, double amount, long accountId) {
        try {
            Account account = accountRepository.findById(accountId).orElse(null);
            
            if (account == null) {
                logger.error("Account not found for ID: " + accountId);
                throw new IllegalArgumentException("Account not found for ID: " + accountId);
            }

            String transactionType = (status == "withdraw") ? "Debit" : "Credit";
            String description = (status == "withdraw") ? "Withdrawal" : "Deposit";

            return createTransaction(amount, transactionType, description, accountId);
        } catch (Exception ex) {
            logger.error("Error while handling funds for account ID: " + accountId, ex);
            throw new RuntimeException("Unable to handle funds for account ID: " + accountId);
        }
    }

    public Boolean transferFunds(long receiverAccountId, double amount, long senderAccountId) {
        try {
            // Handle the transfer of funds between accounts here
            Boolean statusSender = handleFunds("withdraw", amount, senderAccountId);
            Boolean statusReceiver = handleFunds("deposit", amount, receiverAccountId);
            return statusSender && statusReceiver;
        } catch (Exception ex) {
            logger.error("Error while transferring funds from account ID: " + senderAccountId +
                    " to account ID: " + receiverAccountId, ex);
            throw new RuntimeException("Unable to transfer funds.");
        }
    }


}