package com.redmath.mybankingapplication.service;

import com.redmath.mybankingapplication.model.Account;
import com.redmath.mybankingapplication.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final BalanceService balanceService;
    public static final String STATUS_ACTIVE = "ACTIVE";

    @Value("${login.attempts.max:3}")
    private int loginAttemptsMax = 3;

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(AccountRepository accountRepository, BalanceService balanceService) {
        this.accountRepository = accountRepository;
        this.balanceService = balanceService;
    }

    /**
     * Retrieve an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account or null if not found.
     */
    public Account getAccountById(Long id) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if (account.isPresent()) {
                return account.get();
            } else {
                logger.error("Account not found for ID: " + id);
                throw new RuntimeException("Account not found.");
            }
        } catch (Exception ex) {
            logger.error("Error while retrieving an account by ID: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to retrieve the account by ID.");
        }
    }
    /**
     * Get a list of all user accounts.
     *
     * @return A list of accounts.
     */
    public List<Account> getAll() {
        try {
            List<Account> accounts = accountRepository.findAll();
            if (!accounts.isEmpty()) {
                return accounts;
            } else {
                logger.info("No accounts found.");
                return Collections.emptyList(); // Return an empty list when no accounts are found.
            }
        } catch (Exception ex) {
            logger.error("Error while retrieving all accounts: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to retrieve all accounts.");
        }
    }
    /**
     * Create a new user account.
     *
     * @param account The account to be created.
     * @return true if the account was created successfully, false otherwise.
     */
    public Boolean createAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException("Account object cannot be null.");
        }

        try {
            // Hash the password before storing it
//            String hashedPassword = hashPassword(account.getPassword());

            String hashedPassword = account.getPassword();
            account.setPassword("{noop}"+hashedPassword);
            account.setRoles("user");
            Account savedAccount = accountRepository.save(account);
            balanceService.createBalance(savedAccount.getId());
            return true;
        } catch (Exception ex) {
            logger.error("Error while creating an account: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to create the account.");
        }
    }
    /**
     * Update an existing user account.
     *
     * @param accountToUpdate The updated account information.
     * @return The updated account.
     */
    public Account updateAccount(Account accountToUpdate) {
        Account oldAccount = getAccountById(accountToUpdate.getId());
        if (oldAccount == null) {
            logger.error("Account not found for updating.");
            throw new RuntimeException("Account not found for updating.");
        }
        try {
            oldAccount.setEmail(accountToUpdate.getEmail());
            oldAccount.setName(accountToUpdate.getName());
            oldAccount.setAddress(accountToUpdate.getAddress());
            oldAccount.setUsername(accountToUpdate.getUsername());
            oldAccount.setPassword(accountToUpdate.getPassword());
            return accountRepository.save(oldAccount);
        } catch (Exception ex) {
            logger.error("Error while updating an account: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to update the account.");
        }
    }
    /**
     * Delete a user account by ID.
     *
     * @param id The ID of the account to delete.
     * @return A success message.
     */
    public String deleteAccount(long id) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if (account.isPresent()) {
                accountRepository.deleteById(id);
                return "Account deletion successful!";
            } else {
                logger.error("Account not found for deletion.");
                throw new RuntimeException("Account not found for deletion.");
            }
        } catch (Exception ex) {
            logger.error("Error while deleting an account: " + ex.getMessage(), ex);
            throw new RuntimeException("Unable to delete the account.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(userName);
        if (account == null) {
            throw new UsernameNotFoundException("Invalid user: " + userName);
        }
        boolean enabled = true;
        boolean locked = true;
//        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), enabled,
//                true, true, locked, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));

        return new org.springframework.security.core.userdetails.User(account.get().getUsername(),account.get().getPassword(),enabled,true,true,locked, AuthorityUtils.commaSeparatedStringToAuthorityList(account.get().getRoles()));
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public String getRole(String username) {
        Optional<Account> account=accountRepository.findByUsername(username);
        logger.debug("Role is: {}",account.get().getRoles());
        return account.get().getRoles();
    }

}
