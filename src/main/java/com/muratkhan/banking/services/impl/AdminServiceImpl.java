package com.muratkhan.banking.services.impl;

import com.muratkhan.banking.dto.SearchUserRequest;
import com.muratkhan.banking.dto.SearchUserResponse;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.repositories.AdminRepository;
import com.muratkhan.banking.repositories.UserRepository;
import com.muratkhan.banking.services.AdminService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    @Override
    public Page<SearchUserResponse> searchUsers(SearchUserRequest searchUserRequest, Pageable pageable) {
        Page<User> usersPage = adminRepository.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchUserRequest.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchUserRequest.getName().toLowerCase() + "%"));
            }
            if (searchUserRequest.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), searchUserRequest.getEmail()));
            }
            if (searchUserRequest.getPhone() != null) {
                predicates.add(criteriaBuilder.equal(root.get("phone"), searchUserRequest.getPhone()));
            }
            if (searchUserRequest.getBirthDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthDate"), searchUserRequest.getBirthDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return usersPage.map(user -> {
            SearchUserResponse response = new SearchUserResponse();
            response.setId(user.getId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
            response.setBirthDate(user.getBirthDate());
            response.setAccountNonExpired(user.isAccountNonExpired());
            response.setAccountNonLocked(user.isAccountNonLocked());
            response.setCredentialsNonExpired(user.isCredentialsNonExpired());
            response.setEnabled(user.isEnabled());

            if (user.getBankAccount() != null) {
                response.setBankAccountId(user.getBankAccount().getId());
                response.setBalance(user.getBankAccount().getBalance());
            }

            return response;
        });
    }

}
