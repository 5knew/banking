package com.muratkhan.banking.controller;

import com.muratkhan.banking.dto.SearchUserRequest;
import com.muratkhan.banking.dto.SearchUserResponse;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.services.AdminService;
import com.muratkhan.banking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi Admin");
    }

    @PostMapping("/search")
    public Page<SearchUserResponse> searchUsers(@RequestBody SearchUserRequest searchUserRequest, Pageable pageable) {
        return adminService.searchUsers(searchUserRequest, pageable);
    }
}
