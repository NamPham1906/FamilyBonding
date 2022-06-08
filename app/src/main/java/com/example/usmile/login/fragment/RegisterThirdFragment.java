package com.example.usmile.login.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usmile.R;
import com.example.usmile.account.Account;

public class RegisterThirdFragment extends Fragment implements View.OnClickListener{

    EditText accountTextView;
    EditText passwordTextView;
    EditText confirmPasswordTextView;
    Button registerButton;

    Account account;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
