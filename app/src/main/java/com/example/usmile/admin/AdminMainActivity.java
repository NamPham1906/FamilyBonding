package com.example.usmile.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Admin;
import com.example.usmile.account.models.User;
import com.example.usmile.user.fragment.SettingFragment;
import com.example.usmile.user.fragment.TipsFragment;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment = null;
    Admin admin;
    PreferenceManager preferenceManager;
    public int current_id = R.id.admin_action_tips;
    public BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        loadAdminInformation();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, new TipsFragment()).commit();

        navigationView = (BottomNavigationView) findViewById(R.id.admin_bottom_nav);
        navigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id==current_id) return true;

            if (R.id.admin_action_tips == id) {
                fragment = new TipsFragment();
            } else if (R.id.admin_check_clinic == id) {
                showToast("Check clinic");
            } else if (R.id.admin_check_doctor == id) {
                showToast("Check doctor");
            } else if (R.id.admin_check_patient == id) {
                showToast("Check patient");
            } else if (R.id.admin_action_settings == id) {
                showToast("Admin settings");

                Bundle bundle = new Bundle();
                bundle.putString("TYPE", AccountFactory.ADMINSTRING);
                bundle.putSerializable(AccountFactory.ADMINSTRING, admin);

                fragment = new SettingFragment();
                fragment.setArguments(bundle);
            }

            if (fragment != null) {
                current_id = id;
                fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();
            }

            return true;

        });

    }

    private void loadAdminInformation() {
        admin = new Admin();
        admin = (Admin) getIntent().getSerializableExtra(AccountFactory.ADMINSTRING);

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}