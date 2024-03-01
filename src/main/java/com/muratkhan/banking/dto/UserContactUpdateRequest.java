package com.muratkhan.banking.dto;

import lombok.Data;

@Data
public class UserContactUpdateRequest {
    private String email;
    private String phone;
}
