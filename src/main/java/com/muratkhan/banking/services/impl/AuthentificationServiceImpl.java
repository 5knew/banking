package com.muratkhan.banking.services.impl;
import com.muratkhan.banking.dto.JwtAuthenticationResponse;
import com.muratkhan.banking.dto.RefreshTokenRequest;
import com.muratkhan.banking.dto.SignInRequest;
import com.muratkhan.banking.dto.SignUpRequest;
import com.muratkhan.banking.model.BankAccount;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.model.enums.Role;
import com.muratkhan.banking.repositories.UserRepository;
import com.muratkhan.banking.services.AuthenticationService;
import com.muratkhan.banking.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            throw new IllegalArgumentException("Login is already in use");
        }
        if (signUpRequest.getEmail() != null && userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (signUpRequest.getPhone() != null && userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new IllegalArgumentException("Phone is already in use");
        }

        if ((signUpRequest.getLogin().equals(signUpRequest.getEmail())) ||
                (signUpRequest.getLogin().equals(signUpRequest.getPhone())) ||
                (signUpRequest.getEmail() != null && signUpRequest.getEmail().equals(signUpRequest.getPhone()))) {
            throw new IllegalArgumentException("Login, email, and phone must be unique");
        }

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setLogin(signUpRequest.getLogin());
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setBirthDate(signUpRequest.getBirthDate());
        user.setRole(Role.ROLE_USER);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(signUpRequest.getInitialBalance());
        user.setBankAccount(bankAccount);

        return userRepository.save(user);
    }

    public JwtAuthenticationResponse signin(SignInRequest signInRequest) throws IllegalArgumentException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getLogin(),
                signInRequest.getPassword()));

        var user = userRepository.findByLogin(signInRequest.getLogin());

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;

    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userLogin = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByLogin(userLogin);
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }

        return null;
    }
}
