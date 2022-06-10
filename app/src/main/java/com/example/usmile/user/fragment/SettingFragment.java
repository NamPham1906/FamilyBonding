package com.example.usmile.user.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
import com.example.usmile.login.LoginActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SettingFragment extends Fragment implements View.OnClickListener {

    TextView accInfoButton;
    TextView generalSettingButton;
    TextView instructionButton;
    TextView appInfoButton;
    Button logOutButton;
    PreferenceManager preferenceManager;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());
        getBundle();

        accInfoButton = (TextView) view.findViewById(R.id.accInfoTextView);
        generalSettingButton = (TextView) view.findViewById(R.id.generalSettingTextView);

        instructionButton = (TextView) view.findViewById(R.id.instructionTextView);
        appInfoButton = (TextView) view.findViewById(R.id.appInfoTextView);

        logOutButton = (Button) view.findViewById(R.id.logOutBtn);

        accInfoButton.setOnClickListener(this);
        generalSettingButton.setOnClickListener(this);
        instructionButton.setOnClickListener(this);
        appInfoButton.setOnClickListener(this);
        logOutButton.setOnClickListener(this);


    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null)
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        Fragment nextFragment = null;

        switch (id) {
            case R.id.accInfoTextView:

                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountFactory.USERSTRING, user);

                nextFragment = new SettingAccountInfoFragment();
                nextFragment.setArguments(bundle);

                break;
            case R.id.generalSettingTextView:
                showToast("General Settings");
                nextFragment = new SettingGeneralFragment();
                break;
            case R.id.instructionTextView:
                showToast("Instruction");
                nextFragment = new SettingIntructionsFragment();
                break;
            case R.id.appInfoTextView:
                showToast("Application Info");
                nextFragment = new SettingApplicationInfoFragment();

                break;
            case R.id.logOutBtn:
                showToast("Log Out");
                signOut();
                break;
        }

        if (nextFragment != null)
            openNewFragment(nextFragment);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    private void logOut() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }



    private void signOut() {
        showToast("Signing out....");

        preferenceManager.clear();
        getActivity().moveTaskToBack(true);
        getActivity().finish();

    }
}