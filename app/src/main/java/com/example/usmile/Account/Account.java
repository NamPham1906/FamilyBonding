package com.example.usmile.Account;

import java.io.Serializable;

public interface Account extends Serializable {
    String type();
    String username();
    String password ();

    public void login(String user, String password);

}
