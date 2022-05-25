package com.example.usmile.Account;

import com.example.usmile.Account.models.Admin;
import com.example.usmile.Account.models.Doctor;
import com.example.usmile.Account.models.User;

public class AccountFactory {
    public Account getAccount(String accountType) {

        switch(accountType) {
            case "User":
                return new User();
            case "Doctor":
                return new Doctor();
            case "Admin":
                return new Admin();
            default:
                return null;
        }
    }
}
