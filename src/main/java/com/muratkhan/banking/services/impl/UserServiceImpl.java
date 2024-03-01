package com.muratkhan.banking.services.impl;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.repositories.UserRepository;
import com.muratkhan.banking.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
                return userRepository.findByLogin(login);
            }
        };
    }

    @Transactional
    public void updateUserEmail(Long userId, String newEmail) {
        if (newEmail != null && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserPhone(Long userId, String newPhone) {
        if (newPhone != null && userRepository.existsByPhone(newPhone)) {
            throw new IllegalArgumentException("Phone is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPhone(newPhone);
        userRepository.save(user);
    }



    @Transactional
    public void deleteContactInfo(Long userId, boolean emailToDelete, boolean phoneToDelete) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        if (emailToDelete && phoneToDelete) {
            throw new IllegalArgumentException("Cannot delete both email and phone. At least one contact method is required.");
        } else if (emailToDelete) {
            if (user.getPhone() == null || user.getPhone().isEmpty()) {
                throw new IllegalArgumentException("Cannot delete email as no phone number is set.");
            }
            user.setEmail(null);
        } else if (phoneToDelete) {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Cannot delete phone as no email is set.");
            }
            user.setPhone(null);
        }
        userRepository.save(user);
    }

}
