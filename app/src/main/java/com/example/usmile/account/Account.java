package com.example.usmile.account;

import java.io.Serializable;

public interface Account extends Serializable {
    String type();
    String typeVietsub();
    String email();

    String getAccount();
    String getPassword();
    boolean getLocked();
    boolean getDeleted();

    void setEmail(String email);
    void setAccount(String account);
    void setPassword(String password);
    void setLocked(boolean locked);
    void setDeleted(boolean deleted);

    String getFullName();
    String getDOB();
    String getGender();
    String getPhone();
    String getAvatar();

    void setFullName(String fullname);
    void setDOB(String dob);
    void setPhone(String phone);
    void setGender(String gender);
    void setAvatar(String avatar);





}
