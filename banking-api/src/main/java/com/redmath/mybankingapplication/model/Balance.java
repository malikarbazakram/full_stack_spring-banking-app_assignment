package com.redmath.mybankingapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="balance")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long balance_id;

    private double amount;
    private LocalDate date;
    private String balanceType; //credit, debit
    //    @Column(name = "last_updated")
//    private LocalDateTime lastUpdated;
//    @Column(name = "transaction_indicator")
//    private String transactionIndicator;
    @JsonIgnore
    @OneToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;


    public Balance() {
    }

    public Balance(double amount, LocalDate date, String balanceType, Account account) {
        this.amount = amount;
        this.date = date;
        this.balanceType = balanceType;
        this.account = account;
    }

    public long getBalance_id() {
        return balance_id;
    }

    public void setBalance_id(long balance_id) {
        this.balance_id = balance_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
