package com.redmath.mybankingapplication.controller;


import com.redmath.mybankingapplication.model.Account;
import com.redmath.mybankingapplication.model.Transaction;
import com.redmath.mybankingapplication.service.AccountService;
import com.redmath.mybankingapplication.service.BalanceService;
import com.redmath.mybankingapplication.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BalanceService balanceService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(Authentication authentication) {
        try {
            String username=authentication.getName();
            Optional<Account> accountOptional=accountService.findByUsername(username);
            long accountId=accountOptional.get().getId();
            List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
            return ResponseEntity.ok(transactions);
        } catch (Exception ex) {
            logger.error("Error while retrieving transactions for account ID: " + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //http://localhost:8080/api/v1/transactions/w?amount=100&description=WithdrawTesting&aaccountId=1
    //http://localhost:8080/api/v1/transactions/w?amount=100&description=WithdrawTesting
    @PostMapping("/{transactionType}")
    public ResponseEntity<Boolean> createTransaction( Authentication authentication,
            @RequestParam Double amount,
            @PathVariable String transactionType) {
        try {
            String username=authentication.getName();
            Optional<Account> accountOptional=accountService.findByUsername(username);
            long accountId=accountOptional.get().getId();

            Double CurrentBalance= balanceService.getBalanceByAccountId(accountId).getAmount();
            Double UpdatedBalance=CurrentBalance - amount;
            if (transactionType.equalsIgnoreCase("Debit") && UpdatedBalance < 0){
                return ResponseEntity.badRequest().build();
            }
            else{

            // TransactionType is either Debit or Credit
            String description = "";
            if (transactionType.equalsIgnoreCase("credit")){
                description="Deposit";
            }
            else if(transactionType.equalsIgnoreCase("debit")){
                description="Withdrawal";
            }

            Boolean status = transactionService.createTransaction(amount, transactionType, description, accountId);
            return ResponseEntity.ok(status);
            }
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build(); // Invalid input, return bad request
        } catch (Exception ex) {
            logger.error("Error while creating transaction for account ID: " + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    http://localhost:8080/api/v1/transactions/transferFunds?senderAccountId=1&amount=100&receiverAccountId=3
//    http://localhost:8080/api/v1/transactions/transferFunds?&amount=100&receiverAccountId=3
    @PostMapping("/transferfunds")
    public ResponseEntity<Boolean> transferFunds(
            Authentication authentication,
            @RequestParam double amount,
            @RequestParam long receiverAccountId) {
        logger.debug("Reached in transfer Funds Controller");
        try {
            String username=authentication.getName();
            Optional<Account> accountOptional=accountService.findByUsername(username);
            long senderAccountId=accountOptional.get().getId();

            Double CurrentBalance= balanceService.getBalanceByAccountId(senderAccountId).getAmount();
            CurrentBalance = CurrentBalance - amount;
            if (receiverAccountId == senderAccountId || CurrentBalance <=0){
                return ResponseEntity.badRequest().build();
            }
                Boolean status = transactionService.transferFunds(receiverAccountId, amount, senderAccountId);
                return ResponseEntity.ok(status);
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build(); // Invalid input, return bad request
        } catch (Exception ex) {
            logger.error("Error while transferring funds from account ID: "  +
                    " to account ID: " + receiverAccountId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
