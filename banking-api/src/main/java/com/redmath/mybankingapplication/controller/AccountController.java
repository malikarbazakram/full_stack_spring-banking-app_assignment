package com.redmath.mybankingapplication.controller;

import com.redmath.mybankingapplication.model.Account;
import com.redmath.mybankingapplication.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE,RequestMethod.PUT})  // <- use your url of frontend
public class AccountController {
    private final AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAll();
            if (!accounts.isEmpty()) {
                return ResponseEntity.ok(accounts);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception ex) {
            logger.error("Error while retrieving all accounts: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Account> getAccountById(Authentication authentication) {
        try {
            String username=authentication.getName();
            Optional<Account> accountOptional=accountService.findByUsername(username);
            long accountId=accountOptional.get().getId();
            Account account = accountService.getAccountById(accountId);
            if (account != null) {
                logger.info("Account found with Id: {}", accountId);
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            logger.error("Error while retrieving an account by ID: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        try {
            if (accountService.createAccount(account)) {
                return ResponseEntity.status(HttpStatus.OK).body("Account created successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to create the account");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error while creating an account: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> updateAccount(@RequestBody Account account) {
        try {
//            String username=authentication.getName();
//            Optional<Account> accountOptional=accountService.findByUsername(username);
//            long accountId=accountOptional.get().getId();
            account.setId(account.getId());
            Account updatedAccount = accountService.updateAccount(account);
            if (updatedAccount != null) {
                return ResponseEntity.ok("Account updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error while updating an account: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable long accountId) {
        try {
            String result = accountService.deleteAccount(accountId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            logger.error("Error while deleting an account: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    @GetMapping("/role")
    public String getRole(Authentication authentication){
        String username= authentication.getName();
        String role=accountService.getRole(username);
        return role;
    }

}
