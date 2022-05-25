package com.example.usmile.login;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        // call login activity
        try {
            sleep(2000);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}