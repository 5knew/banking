package com.muratkhan.banking.services;

import com.muratkhan.banking.dto.SearchUserRequest;
import com.muratkhan.banking.dto.SearchUserResponse;
import com.muratkhan.banking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AdminService {

    Page<SearchUserResponse> searchUsers(SearchUserRequest searchUserRequest, Pageable pageable);
}
