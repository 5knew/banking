package com.muratkhan.banking.scheduler;
import com.muratkhan.banking.dto.AccountBalanceInfo;
import com.muratkhan.banking.model.BankAccount;
import com.muratkhan.banking.repositories.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Component
public class BalanceUpdater {
    private final BankAccountRepository bankAccountRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void updateAccountBalanceInCache(BankAccount account) {
        String cacheKey = "accountBalance:" + account.getId();
        AccountBalanceInfo balanceInfo = new AccountBalanceInfo();
        balanceInfo.setBalance(account.getBalance());
        balanceInfo.setDepositBalance(account.getDepositBalance());
        redisTemplate.opsForValue().set(cacheKey, balanceInfo);
        log.info("Updated cache for account ID {}: {}", account.getId(), balanceInfo);
    }

    public BalanceUpdater(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        log.info("Starting deposit balance update for {} accounts", accounts.size());

        for (BankAccount account : accounts) {
            double initialDepositBalance = account.getDepositBalance();
            if(account.getDepositBalance() == 0){
                continue;
            }
            double increasedDepositBalance = initialDepositBalance * 1.05;
            if (account.getInitialDeposit() != 0) {
                double maxDepositBalance = account.getInitialDeposit() * 2.07;
                if (increasedDepositBalance > maxDepositBalance) {
                    continue;
                } else {
                    account.setDepositBalance(increasedDepositBalance);
                    bankAccountRepository.save(account);
                    updateAccountBalanceInCache(account);
                }
            } else {
                account.setDepositBalance(increasedDepositBalance);
                bankAccountRepository.save(account);
                updateAccountBalanceInCache(account);
            }
            log.info("Updated deposit balance for account ID {}: from {} to {}", account.getId(), initialDepositBalance, account.getDepositBalance());
        }

        log.info("Deposit balance update completed");
    }

}



