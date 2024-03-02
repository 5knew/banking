package com.muratkhan.banking.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SearchUserResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthDate;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private Long bankAccountId;
    private Double balance;
    private Double deposit;
}
