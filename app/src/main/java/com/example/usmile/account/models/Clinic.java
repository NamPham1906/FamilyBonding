package com.example.usmile.account.models;

import android.location.Location;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class Clinic implements Account {

    private String id;
    private float latitude;
    private float longitude;
    private String address;
    private String fullname;
    private String avatar;
    private String phone;
    private String dob;
    private String gender;
    private String email;
    private String account;
    private String password;
    private boolean deleted;
    private boolean locked;


    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String type() {
        return AccountFactory.CLINICSTRING;
    }
    @Override
    public String typeVietsub() {
        return null;
    }

    public float getLatitude(){
        return latitude;
    }
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }

    public float getLongitude(){
        return longitude;
    }
    public void setLongitude(float longitude){
        this.longitude = longitude;
    }

    public void setAddress(String address){ this.address = address; }
    public String getAddress(){ return this.address; }


    @Override
    public String email() {
        return this.email;
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean getLocked() {
        return this.locked;
    }

    @Override
    public boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setAccount(String account) {
        this.account = account;
    }

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
    public String getGender() { return gender; }

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
