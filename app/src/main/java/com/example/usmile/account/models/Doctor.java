package com.example.usmile.account.models;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class Doctor implements Account {
    private String email;
    @Override
    public String type() {
        return AccountFactory.DOCTORSTRING;
    }

    @Override
    public String email() {
        return this.email;
    }

    public Doctor (String email){
        this.email = email;
    }


}
