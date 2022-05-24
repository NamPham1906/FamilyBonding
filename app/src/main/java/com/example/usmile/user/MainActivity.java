package com.example.usmile.user;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.fragment.CollectPictureFragment;
import com.example.usmile.user.fragment.SettingFragment;
import com.example.usmile.user.fragment.WelcomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager = getSupportFragmentManager();

        // welcome fragment
        fragment = new WelcomeFragment();
        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (R.id.action_tips == id) {
                Toast.makeText(MainActivity.this, "Tips", Toast.LENGTH_SHORT).show();
                //fragment = new HomepageFragment(this);
            }
            else if (R.id.action_document == id) {
                Toast.makeText(MainActivity.this, "Docs", Toast.LENGTH_SHORT).show();
                //fragment = new CategoriesFragment();
            }

            else if (R.id.action_take_picture == id) {
                Toast.makeText(MainActivity.this, "Cam", Toast.LENGTH_SHORT).show();
                fragment = new CollectPictureFragment();
            }

            else if (R.id.action_find_clinic == id) {
                Toast.makeText(MainActivity.this, "Clinic", Toast.LENGTH_SHORT).show();
            }

            else if (R.id.action_settings == id) {
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                fragment = new SettingFragment();
            }

            if (fragment != null) {
                fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();
            }


            return true;
        });
    }

}