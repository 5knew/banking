package com.muratkhan.banking.repositories;
import com.muratkhan.banking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
