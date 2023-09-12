package com.redmath.mybankingapplication.controller;


import com.redmath.mybankingapplication.model.Account;
import com.redmath.mybankingapplication.model.Balance;
import com.redmath.mybankingapplication.service.AccountService;
import com.redmath.mybankingapplication.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/balance")
@CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE,RequestMethod.PUT})  // <- use your url of frontend
public class BalanceController {
    private final BalanceService balanceService;
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    @Autowired
    private AccountService accountService;

    public BalanceController (BalanceService balanceService){
        this.balanceService = balanceService;
    }


    @GetMapping("/getBalance")
    public ResponseEntity<Double> getBalanceByAccountId(Authentication authentication) {
        try {
            String username=authentication.getName();
            Optional<Account> accountOptional=accountService.findByUsername(username);
            long id=accountOptional.get().getId();

            Balance returnBalance = balanceService.getBalanceByAccountId(id);
            return ResponseEntity.ok(returnBalance.getAmount());
        } catch (RuntimeException ex) {
            logger.error("Error while retrieving balance by Account ID: " + ". " + ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Balance>>> findAll(Authentication authentication) {

        List<Balance> balances;
        String username=authentication.getName();
        Optional<Account> accountOptional=accountService.findByUsername(username);
        long accountId=accountOptional.get().getId();
        balances = balanceService.findAllBalances(accountId);

        if (balances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("content", balances));
    }

}
