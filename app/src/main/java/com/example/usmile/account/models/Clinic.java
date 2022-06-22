package com.example.usmile.account.models;

import android.location.Location;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;

public class Clinic implements Account {

    private String name;
    private float latitude;
    private float longitude;


    @Override
    public String type() {
        return AccountFactory.CLINICSTRING;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    @Override
    public String typeVietsub() {
        return null;
    }

    @Override
    public String email() {
        return null;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean getLocked() {
        return false;
    }

    @Override
    public boolean getDeleted() {
        return false;
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public void setAccount(String account) {

    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public void setLocked(boolean locked) {

    }

    @Override
    public void setDeleted(boolean deleted) {

    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public String getDOB() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public void setFullName(String fullname) {

    }

    @Override
    public void setDOB(String dob) {

    }

    @Override
    public void setPhone(String phone) {

    }

    @Override
    public void setGender(String gender) {

    }

    @Override
    public void setAvatar(String avatar) {

    }
}
