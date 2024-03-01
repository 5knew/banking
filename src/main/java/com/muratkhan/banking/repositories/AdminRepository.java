package com.muratkhan.banking.repositories;

import com.muratkhan.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}