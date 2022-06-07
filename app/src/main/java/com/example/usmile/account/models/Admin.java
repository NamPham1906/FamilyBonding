package com.example.usmile.account.models;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class Admin implements Account  {
    private String email;
    @Override
    public String type() {
        return AccountFactory.ADMINSTRING;
    }

    @Override
    public String typeVietsub() {
        return AccountFactory.ADMINSTRING_VN;
    }

    @Override
    public String email() {
        return this.email;
    }

    public Admin(String email){
        this.email = email;
    }
}
