package com.example.usmile.account;

import com.example.usmile.account.models.Admin;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.account.models.User;
import com.example.usmile.doctor.DoctorMainActivity;
import com.example.usmile.user.UserMainActivity;

public class AccountFactory {
    final static public String USERSTRING = "User";
    final static public String DOCTORSTRING = "Doctor";
    final static public String ADMINSTRING = "Admin";

    final static public String USERSTRING_VN = "Người dùng cá nhân";
    final static public String DOCTORSTRING_VN = "Bác sĩ";
    final static public String ADMINSTRING_VN = "Quản trị";


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
        if (accountType == null) return null;
        switch(accountType) {
            case USERSTRING:
                return UserMainActivity.class;
            case DOCTORSTRING:
                return DoctorMainActivity.class;
            case ADMINSTRING:
                //return AdminMainActivity.class;
            default:
                return null;
        }
    }
}
