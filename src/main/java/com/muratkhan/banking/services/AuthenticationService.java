package com.muratkhan.banking.services;


import com.muratkhan.banking.dto.JwtAuthenticationResponse;
import com.muratkhan.banking.dto.RefreshTokenRequest;
import com.muratkhan.banking.dto.SignInRequest;
import com.muratkhan.banking.dto.SignUpRequest;
import com.muratkhan.banking.model.User;

public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
