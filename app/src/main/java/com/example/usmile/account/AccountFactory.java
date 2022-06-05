package com.example.usmile.account;

import com.example.usmile.account.models.Admin;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.account.models.User;
import com.example.usmile.user.UserMainActivity;

public class AccountFactory {
    final static public String USERSTRING = "User";
    final static public String DOCTORSTRING = "Doctor";
    final static public String ADMINSTRING = "Admin";
    public static Account createAccount(String accountType, String email) {

        switch(accountType) {
            case USERSTRING:
                return new User(email);
            case DOCTORSTRING:
                return new Doctor(email);
            case ADMINSTRING:
                return new Admin(email);
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
