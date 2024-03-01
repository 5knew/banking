package com.muratkhan.banking.scheduler;
import com.muratkhan.banking.model.BankAccount;
import com.muratkhan.banking.repositories.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Component
public class BalanceUpdater {
    private final BankAccountRepository bankAccountRepository;

    public BalanceUpdater(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        log.info("Starting deposit balance update for {} accounts", accounts.size());

        for (BankAccount account : accounts) {
            double initialDepositBalance = account.getDepositBalance() != null ? account.getDepositBalance() : 0.0;
            double increasedDepositBalance = initialDepositBalance * 1.05; // Умножаем депозитный баланс на 1.05

            account.setDepositBalance(increasedDepositBalance); // Обновляем депозитный баланс

            bankAccountRepository.save(account);
            log.info("Updated deposit balance for account ID {}: from {} to {}", account.getId(), initialDepositBalance, increasedDepositBalance);
        }

        log.info("Deposit balance update completed");
    }
}


