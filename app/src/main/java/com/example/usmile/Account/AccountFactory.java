package com.example.usmile.Account;

import com.example.usmile.Account.models.Admin;
import com.example.usmile.Account.models.Doctor;
import com.example.usmile.Account.models.User;

public class AccountFactory {
    final static public String USERSTRING = "User";
    final static public String DOCTORSTRING = "Doctor";
    final static public String ADMINSTRING = "Admin";
    public static Account createAccount(String accountType) {

        switch(accountType) {
            case USERSTRING:
                return new User();
            case DOCTORSTRING:
                return new Doctor();
            case ADMINSTRING:
                return new Admin();
            default:
                return null;
        }
    }
}
