package com.muratkhan.banking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String email;
    private String phone;
    private Date birthDate;
    private Double initialBalance;

}
