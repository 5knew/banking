package com.muratkhan.banking.services;
import com.muratkhan.banking.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService {
    UserDetailsService userDetailsService();
    void updateUserEmail(Long userId, String newEmail);
    void updateUserPhone(Long userId, String newPhone);
    void deleteContactInfo(Long userId, boolean emailToDelete, boolean phoneToDelete);

}
