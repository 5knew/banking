package com.muratkhan.banking.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bank_accounts")
@Data
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "initial_deposit")
    private Double initialDeposit = 0.0;

    @Column(name = "deposit_balance")
    private Double depositBalance = 0.0;

    @Version
    private Long version = 0L;

}

