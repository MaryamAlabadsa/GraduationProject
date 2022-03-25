package com.example.graduationproject.requests;

public class ChangePassBody {
    String old_password, password, password_confirmation;

    public ChangePassBody(String old_password, String password, String password_confirmation) {
        this.old_password = old_password;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
