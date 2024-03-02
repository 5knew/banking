package com.muratkhan.banking.dto;

import lombok.Data;

@Data
public class TransferRequestByLogin {

    private String fromUserLogin;
    private String toUserLogin;
    private Double amount;

}