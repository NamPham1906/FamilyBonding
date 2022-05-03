package com.example.usmile.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.usmile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_tips:
                        Toast.makeText(MainActivity.this, "Tips", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_document:
                        Toast.makeText(MainActivity.this, "Docs", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_take_picture:
                        Toast.makeText(MainActivity.this, "Cam", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_find_clinic:
                        Toast.makeText(MainActivity.this, "Clinic", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }
}