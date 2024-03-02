package com.muratkhan.banking.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String login;
    private String password;

}
