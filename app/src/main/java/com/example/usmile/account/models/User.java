package com.example.usmile.account.models;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class User implements Account {
    private String email;
    @Override
    public String type() {
        return AccountFactory.USERSTRING;
    }

    @Override
    public String email() {
        return this.email;
    }

    public User(String email){
        this.email = email;
    }
}
