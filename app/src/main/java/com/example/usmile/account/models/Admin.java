package com.example.usmile.account.models;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class Admin implements Account  {
    private String id;
    private String email;
    private String account;
    private String password;
    private boolean deleted;
    private boolean locked;

    private String fullname;
    private String dob;
    private String gender;
    private String phone;
    private String avatar;

    @Override
    public String id() {
        return this.id;
    }

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



    public Admin(){

    }


    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean getLocked() {
        return locked;
    }

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public void setEmail(String email) { this.email = email; }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getFullName() {
        return fullname;
    }

    @Override
    public String getDOB() {
        return dob;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public void setDOB(String dob) {
        this.dob = dob;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
