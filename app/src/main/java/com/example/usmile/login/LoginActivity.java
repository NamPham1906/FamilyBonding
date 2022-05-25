package com.example.usmile.login;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.usmile.Account.Account;
import com.example.usmile.Account.AccountFactory;
import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    FragmentManager fragmentManager;
    Fragment fragment = null;
    String accountType = "";
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton =  this.findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();

    }



    @Override
    public void onClick(View v) {
        accountType = AccountFactory.USERSTRING;

        Intent intent = new Intent(getApplicationContext(), AccountFactory.createAccountClass(accountType));
        Account account = AccountFactory.createAccount(accountType);
        intent.putExtra(account.type(), account);
        startActivity(intent);
        this.finish();
    }
}