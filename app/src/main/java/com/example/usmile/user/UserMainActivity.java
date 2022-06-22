package com.example.usmile.user;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
import com.example.usmile.R;
import com.example.usmile.user.adapters.MultiHealthRecordAdapter;
import com.example.usmile.user.fragment.CollectPictureFragment;
import com.example.usmile.user.fragment.DetailAcceptedHealthRecordFragment;
import com.example.usmile.user.fragment.HealthRecordFragment;
import com.example.usmile.user.fragment.MapFragment;
import com.example.usmile.user.fragment.SettingFragment;
import com.example.usmile.user.fragment.TipsFragment;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment = null;
    User user;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = (User) getIntent().getSerializableExtra(AccountFactory.USERSTRING);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        loadUserInformation();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, new TipsFragment()).commit();

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnItemSelectedListener(item -> {

                    int id = item.getItemId();

                    if (R.id.action_tips == id) {
                        Toast.makeText(UserMainActivity.this, "Tips", Toast.LENGTH_SHORT).show();
                        fragment = new TipsFragment();
                    }
                    else if (R.id.action_document == id) {
                        Toast.makeText(UserMainActivity.this, "Docs", Toast.LENGTH_SHORT).show();
                        fragment = new HealthRecordFragment();
                    }

                    else if (R.id.action_take_picture == id) {
                        Toast.makeText(UserMainActivity.this, "Cam", Toast.LENGTH_SHORT).show();
                        fragment = new CollectPictureFragment();
                    }

                    else if (R.id.action_find_clinic == id) {
                        Toast.makeText(UserMainActivity.this, "Clinic", Toast.LENGTH_SHORT).show();
                        fragment = new MapFragment();
                    }

                    else if (R.id.action_settings == id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("TYPE", AccountFactory.USERSTRING);
                        bundle.putSerializable(AccountFactory.USERSTRING, user);

                        fragment = new SettingFragment();
                        fragment.setArguments(bundle);
                    }

                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();
                    }
                    return true;
        });
    }

    private void loadUserInformation() {
        user = new User();
        user = (User) getIntent().getSerializableExtra(AccountFactory.USERSTRING);

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }




}