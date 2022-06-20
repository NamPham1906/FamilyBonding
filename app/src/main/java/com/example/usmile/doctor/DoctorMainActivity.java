package com.example.usmile.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.account.models.User;
import com.example.usmile.doctor.fragment.WaitingHealthRecordListFragment;
import com.example.usmile.user.fragment.SettingFragment;
import com.example.usmile.user.fragment.TipsFragment;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorMainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment = null;

    Doctor doctor;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        doctor = (Doctor) getIntent().getSerializableExtra(AccountFactory.DOCTORSTRING);

        preferenceManager = new PreferenceManager(getApplicationContext());

        loadDoctorInformation();
        showToast(doctor.getFullName());

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, new TipsFragment()).commit();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.doc_bottom_nav);

        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.doc_action_tips:

                    fragment = new TipsFragment();
                    break;
                case R.id.action_check_history:
                    showToast("Check history");
                    break;
                case R.id.action_give_advices:
                    showToast("Waiting HealthRecord");
                    fragment = new WaitingHealthRecordListFragment();

                    break;
                case R.id.doc_action_settings:


                    Bundle bundle = new Bundle();
                    bundle.putString("TYPE", AccountFactory.DOCTORSTRING);
                    bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);

                    fragment = new SettingFragment();
                    fragment.setArguments(bundle);
                    break;
            }

            if (fragment != null) {
                fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, fragment).commit();
            }


            return true;
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void loadDoctorInformation() {
        doctor = new Doctor();

        doctor.setAccount(preferenceManager.getString(Constants.KEY_ACCOUNT_ACCOUNT));
        doctor.setPassword(preferenceManager.getString(Constants.KEY_ACCOUNT_PASSWORD));

        doctor.setFullName(preferenceManager.getString(Constants.KEY_ACCOUNT_FULL_NAME));
        doctor.setDOB(preferenceManager.getString(Constants.KEY_ACCOUNT_DOB));
        doctor.setPhone(preferenceManager.getString(Constants.KEY_ACCOUNT_PHONE));
        doctor.setGender(preferenceManager.getString(Constants.KEY_ACCOUNT_GENDER));

        doctor.setAvatar(preferenceManager.getString(Constants.KEY_ACCOUNT_AVATAR));

        doctor.setWorkPlace(preferenceManager.getString(Constants.KEY_ACCOUNT_WORKPLACE));

    }
}