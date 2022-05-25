package com.example.usmile.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.usmile.Account.Account;
import com.example.usmile.Account.AccountFactory;
import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;

public class LoginActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment = null;
    String accountType = "";
    Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();


        accountType = AccountFactory.USERSTRING;

        Intent intent = new Intent(getApplicationContext(), AccountFactory.createAccountClass(accountType));
        account = AccountFactory.createAccount(accountType);
        intent.putExtra(account.type(), account);
        startActivity(intent);
        this.finish();




    }

    public void startHomeActivity (String type){

    }
}