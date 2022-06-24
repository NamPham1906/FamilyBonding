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
import com.example.usmile.doctor.fragment.ReceivedHealthRecordListFragment;
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
    public int current_id =  R.id.doc_action_tips;
    public BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        loadDoctorInformation();
        showToast(doctor.getFullName());

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.mainFragmentHolder, new TipsFragment()).commit();

        navigationView = (BottomNavigationView) findViewById(R.id.doc_bottom_nav);

        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id==current_id) return true;
            Bundle bundle = new Bundle();
            switch (id) {
                case R.id.doc_action_tips:

                    fragment = new TipsFragment();
                    break;
                case R.id.action_check_history:
                    showToast("Received HealthRecord");
                    bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);

                    fragment = new ReceivedHealthRecordListFragment();
                    fragment.setArguments(bundle);
                    break;
                case R.id.action_give_advices:
                    showToast("Waiting HealthRecord");
                    bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);
                    fragment = new WaitingHealthRecordListFragment();
                    fragment.setArguments(bundle);
                    break;
                case R.id.doc_action_settings:


                    bundle.putString("TYPE", AccountFactory.DOCTORSTRING);
                    bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);

                    fragment = new SettingFragment();
                    fragment.setArguments(bundle);
                    break;
            }

            if (fragment != null) {
                current_id = id;
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
        doctor = (Doctor) getIntent().getSerializableExtra(AccountFactory.DOCTORSTRING);
    }
}