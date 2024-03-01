package com.muratkhan.banking.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private String toUserLogin;
    private Double amount;
}