package com.redmath.mybankingapplication.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
//    @JsonIgnore
    String password;
    private String name;
    private String email;
    private String address;
    private String role;

    // One-to-One relationship with Balance
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Balance balance;

    // One-to-Many relationship with Transaction
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(String username, String password, String name, String email, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoles() {
    return role;}
    public void setRoles(String role){
        this.role=role;
    }
}