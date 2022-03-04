package com.example.graduationproject.requests;

public class LoginRequestBody {
    String email,password;

    public LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
