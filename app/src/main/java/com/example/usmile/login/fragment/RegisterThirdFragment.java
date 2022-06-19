package com.example.usmile.login.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usmile.R;
import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.doctor.DoctorMainActivity;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterThirdFragment extends Fragment implements View.OnClickListener{

    EditText accountTextView;
    EditText passwordTextView;
    EditText confirmPasswordTextView;
    Button registerButton;
    ProgressBar progressBar;

    Account account;
    PreferenceManager preferenceManager;
    FirebaseAuth mAuth  = FirebaseAuth.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        getBundle();

        bindingView(view);

        setListeners();
    }

    private void setListeners() {
        registerButton.setOnClickListener(this);
    }

    private void bindingView(@NonNull View view) {
        accountTextView = (EditText) view.findViewById(R.id.accountTV);
        passwordTextView = (EditText) view.findViewById(R.id.passTextView);
        confirmPasswordTextView = (EditText) view.findViewById(R.id.confirmPassTV);
        registerButton = (Button) view.findViewById(R.id.registerButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_third, container, false);
    }

    private boolean isCompleted() {
        if (!isFillEditText(accountTextView))
            return false;
        if (!isFillEditText(passwordTextView))
            return false;
        if (!isFillEditText(confirmPasswordTextView))
            return false;

        String accountText = accountTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();
        String confirmPass = confirmPasswordTextView.getText().toString().trim();

        if (password.equals(confirmPass)) {
            account.setAccount(accountText);
            account.setPassword(password);
            account.setLocked(false);
            account.setDeleted(false);

            return true;
        } else {
            confirmPasswordTextView.setError("Mật khẩu không khớp");
            passwordTextView.setError("Mật khẩu không khớp");
            return false;
        }

    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null)
            account = (Account) bundle.getSerializable("NEW_ACCOUNT");

        String msg = "Account full name: " + account.getFullName();
        if (account != null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private boolean isFillEditText(EditText editText) {
        String input = editText.getText().toString().trim();

        if (input.isEmpty()) {
            editText.setError("Không được để trống");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.registerButton:

                if (!isCompleted())
                    return;

                signUp();

                break;
        }
    }

    private void signUp() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(account.email(),account.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showToast("Sign up complete");
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    HashMap<String, Object> newAccount = new HashMap<>();
                    newAccount.put(Constants.KEY_ACCOUNT_TYPE, account.type());

                    newAccount.put(Constants.KEY_ACCOUNT_AVATAR, account.getAvatar());
                    newAccount.put(Constants.KEY_ACCOUNT_FULL_NAME, account.getFullName());
                    newAccount.put(Constants.KEY_ACCOUNT_DOB, account.getDOB());
                    newAccount.put(Constants.KEY_ACCOUNT_PHONE, account.getPhone());
                    newAccount.put(Constants.KEY_ACCOUNT_GENDER, account.getGender());

                    newAccount.put(Constants.KEY_ACCOUNT_ACCOUNT, account.getAccount());
                    newAccount.put(Constants.KEY_ACCOUNT_EMAIL, account.email());
                   // newAccount.put(Constants.KEY_ACCOUNT_PASSWORD, account.getPassword());

                    database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                            .add(newAccount)
                            .addOnSuccessListener(documentReference -> {

                                progressBar.setVisibility(View.INVISIBLE);
                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                preferenceManager.putString(Constants.KEY_ACCOUNT_ID, documentReference.getId());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_TYPE, account.type());

                                preferenceManager.putString(Constants.KEY_ACCOUNT_AVATAR, account.getAvatar());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_FULL_NAME, account.getFullName());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_DOB,  account.getDOB());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_GENDER, account.getAccount());

                                preferenceManager.putString(Constants.KEY_ACCOUNT_ACCOUNT, account.getAccount());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_EMAIL, account.email());
                                preferenceManager.putString(Constants.KEY_ACCOUNT_PASSWORD, account.getPassword());



                                if (account.type() == AccountFactory.USERSTRING) {
                                    Intent intent = new Intent(getContext(), UserMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else if (account.type() == AccountFactory.DOCTORSTRING) {
                                    Intent intent = new Intent(getContext(), DoctorMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(exception -> {
                                showToast(exception.getMessage());
                            });
                } else {
                    showToast(task.getException().toString());
                    showToast("Sign up unsuccessfully!");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
