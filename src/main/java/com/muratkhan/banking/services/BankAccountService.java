package com.muratkhan.banking.services;

import com.muratkhan.banking.dto.AccountBalanceInfo;

public interface BankAccountService {
    void transferMoneyByLogin(String fromUserLogin, String toUserLogin, Double amount);
    void transferToDeposit(Long userId, Double amount);
    AccountBalanceInfo getAccountBalanceInfo(Long userId);
}
