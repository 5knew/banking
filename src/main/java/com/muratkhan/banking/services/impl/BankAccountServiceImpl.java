package com.muratkhan.banking.services.impl;

import com.muratkhan.banking.dto.AccountBalanceInfo;
import com.muratkhan.banking.model.BankAccount;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.repositories.BankAccountRepository;
import com.muratkhan.banking.repositories.UserRepository;
import com.muratkhan.banking.services.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;


    @Transactional
    public void transferMoneyByLogin(String fromUserLogin, String toUserLogin, Double amount) {
        User fromUser = userRepository.findByLogin(fromUserLogin);
        if (fromUser == null) {
            throw new IllegalArgumentException("Source user not found by login: " + fromUserLogin);
        }

        User toUser = userRepository.findByLogin(toUserLogin);
        if (toUser == null) {
            throw new IllegalArgumentException("Target user not found by login: " + toUserLogin);
        }

        BankAccount fromAccount = fromUser.getBankAccount();
        BankAccount toAccount = toUser.getBankAccount();

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("One or both users do not have a bank account");
        }

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds on source account");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    @Transactional
    public void transferToDeposit(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BankAccount account = user.getBankAccount();

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds on the account");
        }

        account.setBalance(account.getBalance() - amount);
        account.setDepositBalance(account.getDepositBalance() + amount);

        bankAccountRepository.save(account);
    }

    public AccountBalanceInfo getAccountBalanceInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BankAccount account = user.getBankAccount();
        if (account == null) {
            throw new IllegalArgumentException("Bank account not found for user: " + userId);
        }

        AccountBalanceInfo balanceInfo = new AccountBalanceInfo();
        balanceInfo.setBalance(account.getBalance());
        balanceInfo.setDepositBalance(account.getDepositBalance());

        return balanceInfo;
    }


}
