package com.example.usmile.Account.models;

import com.example.usmile.Account.Account;

public class Admin implements Account  {
    @Override
    public String type() {
        return "Admin";
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public String password() {
        return null;
    }

    @Override
    public void login(String user, String password) {

    }
}
