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
    String login = "";
    Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();

        login = AccountFactory.USERSTRING;

        switch (login){
            case (AccountFactory.USERSTRING):
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                account = AccountFactory.createAccount(AccountFactory.USERSTRING);
                intent.putExtra(account.type(), account);
                startActivity(intent);
                this.finish();
                break;
            case(AccountFactory.DOCTORSTRING):
                account = AccountFactory.createAccount(AccountFactory.DOCTORSTRING);
                break;
            case(AccountFactory.ADMINSTRING):
                account = AccountFactory.createAccount(AccountFactory.ADMINSTRING);
                break;
            default:
                break;
        }

       // fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();

    }

    public void startHomeActivity (String type){

    }
}