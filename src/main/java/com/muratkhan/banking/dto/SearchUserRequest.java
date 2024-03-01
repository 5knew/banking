package com.muratkhan.banking.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchUserRequest {
    String name;
    String email;
    String phone;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthDate;
}
