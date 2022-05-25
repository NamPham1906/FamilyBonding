package com.example.usmile.Account.models;

import com.example.usmile.Account.Account;
import com.example.usmile.Account.AccountFactory;

public class User implements Account {
    @Override
    public String type() {
        return AccountFactory.USERSTRING;
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
