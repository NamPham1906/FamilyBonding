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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Admin;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.account.models.User;
import com.example.usmile.doctor.fragment.SettingDoctorAccountInfoFragment;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SettingChangePasswordFragment extends Fragment implements View.OnClickListener{

    Account account;
    EditText currentPasswordEditText;
    EditText newPasswordEditText;
    EditText confirmPasswordEditText;
    ProgressBar progressBar;
    Button changeButton;
    Button forgetPasswordButton;
    private FirebaseAuth mAuth;

    PreferenceManager preferenceManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getContext());

        getBundle();
        bindingView(view);
        setListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_change_password, container, false);
    }

    private void bindingView(@NonNull View view) {
        currentPasswordEditText = (EditText) view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        changeButton = (Button) view.findViewById(R.id.changeButton);
        forgetPasswordButton = (Button) view.findViewById(R.id.forgetPasswordBtn);
    }

    private void setListeners() {
        changeButton.setOnClickListener(this);
        forgetPasswordButton.setOnClickListener(this);
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            String type = bundle.getString("TYPE");

            if (type.equals(AccountFactory.USERSTRING)) {
                account = (User) bundle.getSerializable(AccountFactory.USERSTRING);
            } else if (type.equals(AccountFactory.DOCTORSTRING)) {
                account = (Doctor) bundle.getSerializable(AccountFactory.DOCTORSTRING);
            } else if (type.equals(AccountFactory.ADMINSTRING)) {
                account = (Admin) bundle.getSerializable(AccountFactory.ADMINSTRING);
            }

        }

        //if (bundle != null)
            //user = (User) bundle.getSerializable(AccountFactory.USERSTRING);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.changeButton:

                if (!isAllFilled())
                    return;

                progressBar.setVisibility(View.VISIBLE);
                String inputPass = currentPasswordEditText.getText().toString().trim();


                AuthCredential credential = EmailAuthProvider
                        .getCredential(account.email(), inputPass);

                mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            currentPasswordEditText.setError(null);
                            if (checkNewPasswordMatch()){
                                updatePassword();

                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            currentPasswordEditText.setError("Mật khẩu không đúng");
                            return;
                        }
                    }
                });
                break;
            case R.id.forgetPasswordBtn:
                break;
        }
    }

    private boolean isAllFilled() {
        boolean filled = true;

        String inputPass = currentPasswordEditText.getText().toString().trim();
        String pass = newPasswordEditText.getText().toString().trim();
        String confirm = confirmPasswordEditText.getText().toString().trim();

        String error = "Không được để trống";

        if (inputPass.isEmpty()) {
            filled = false;
            currentPasswordEditText.setError(error);
        }

        if (pass.isEmpty()) {
            filled = false;
            newPasswordEditText.setError(error);
        }

        if (confirm.isEmpty()) {
            filled = false;
            confirmPasswordEditText.setError(error);
        }

        return filled;
    }

    //private boolean checkCurrentPassword() {

   // }

    private boolean checkNewPasswordMatch() {
        String pass = newPasswordEditText.getText().toString().trim();
        String confirm = confirmPasswordEditText.getText().toString().trim();

        if (pass.equals(confirm)) {
            newPasswordEditText.setError(null);
            confirmPasswordEditText.setError(null);
            return true;
        }

        newPasswordEditText.setError("Mật khẩu không giống với xác nhận");
        confirmPasswordEditText.setError("Mật khẩu xác nhận chưa chính xác");
        return false;
    }

    private void updatePassword() {
        String password = newPasswordEditText.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Bundle newbundle = new Bundle();
                            newbundle.putSerializable(account.type(), account);
                            Fragment newFragment;
                            if (account.type().equals(AccountFactory.DOCTORSTRING)) {
                                newFragment = new SettingDoctorAccountInfoFragment();
                            } else{
                                newFragment = new SettingAccountInfoFragment();
                            }
                            newFragment.setArguments(newbundle);
                            openNewFragment(newFragment);
                            showToast("Updated password successfully");
                        } else {
                            showToast("Unable to update password");
                        }
                    }
                });

        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(preferenceManager.getString(Constants.KEY_ACCOUNT_ID));

        HashMap<String, Object> updates = new HashMap<>();

        updates.put(Constants.KEY_ACCOUNT_PASSWORD, password);

        documentReference.update(updates)
                .addOnSuccessListener(unused -> {

                    preferenceManager.putString(Constants.KEY_ACCOUNT_PASSWORD, password);

                    showToast("Updated password successfully");

                    Intent intent = new Intent(getContext(), UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    showToast("Unable to update password");
                });*/
    }

    private void showToast(String msg) {
        if (msg!=null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}