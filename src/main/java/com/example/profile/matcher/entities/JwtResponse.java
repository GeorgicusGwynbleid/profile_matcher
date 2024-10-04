package com.example.profile.matcher.entities;

public class JwtResponse {
    private String token;
    private String type = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }

    // Getters
    public String getAccessToken() {
        return token;
    }

    public String getTokenType() {
        return type;
    }
}
