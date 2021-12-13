package com.example.apiuseramt.payload.response;

public class JwtResponse {
    final private String token;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }

    public String getAccessToken() {
        return token;
    }

    public String getTokenType() {
        return "Bearer";
    }
}

