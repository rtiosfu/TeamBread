package com.example.loginscreen;

public class User {
    public String email;
    public String password;

    public User() {
        this.email = "";
        this.password = "";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
