package com.example.profile.matcher.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    public String authenticateUser(String username, String password) {
        return tokenGenerator.generateToken(username);
    }

}