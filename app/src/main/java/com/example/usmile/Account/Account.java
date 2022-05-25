package com.example.usmile.Account;

public interface Account {
    String type();
    String username();
    String password ();

    public void login(String user, String password);

}
