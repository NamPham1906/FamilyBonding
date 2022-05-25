package com.example.usmile.Account;

import com.example.usmile.Account.models.Admin;
import com.example.usmile.Account.models.Doctor;
import com.example.usmile.Account.models.User;
import com.example.usmile.user.UserMainActivity;

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

    public static Class<?> createAccountClass(String accountType) {

        switch(accountType) {
            case USERSTRING:
                return UserMainActivity.class;
            case DOCTORSTRING:
                //return DoctorMainActivity.class;
            case ADMINSTRING:
                //return AdminMainActivity.class;
            default:
                return null;
        }
    }
}
