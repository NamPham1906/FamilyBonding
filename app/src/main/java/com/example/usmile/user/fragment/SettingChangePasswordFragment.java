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
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SettingChangePasswordFragment extends Fragment implements View.OnClickListener{

    User user;
    EditText currentPasswordEditText;
    EditText newPasswordEditText;
    EditText confirmPasswordEditText;

    Button changeButton;
    Button forgetPasswordButton;

    PreferenceManager preferenceManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        changeButton = (Button) view.findViewById(R.id.changeButton);
        forgetPasswordButton = (Button) view.findViewById(R.id.forgetPasswordBtn);
    }

    private void setListeners() {
        changeButton.setOnClickListener(this);
        forgetPasswordButton.setOnClickListener(this);
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null)
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.changeButton:

                if (!isAllFilled())
                    return;

                if (!checkCurrentPassword())
                    return;

                if (!checkNewPasswordMatch())
                    return;

                updatePassword();

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

    private boolean checkCurrentPassword() {
        String inputPass = currentPasswordEditText.getText().toString().trim();

        if (inputPass.equals(user.getPassword())) {
            currentPasswordEditText.setError(null);
            return true;
        }


        currentPasswordEditText.setError("Mật khẩu không đúng");
        return false;
    }

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
                        if (task.isSuccessful()) {
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
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}