package com.example.usmile.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;

public class LoginActivity extends AppCompatActivity {

    final int USER = 1;
    final int DOCTOR = 2;
    final int ADMIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int login = 0;

        login = 1;
        if (login == USER){
            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
            startActivity(intent);
        }


        this.finish();
    }
}