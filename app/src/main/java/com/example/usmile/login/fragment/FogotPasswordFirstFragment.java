package com.example.usmile.login.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.usmile.R;
import com.example.usmile.login.LoginActivity;
import com.example.usmile.login.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

public class FogotPasswordFirstFragment extends Fragment implements  View.OnClickListener {

    EditText emailText;
    Button contButton;
    ProgressBar progressBar;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        setListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fogot_password_first, container, false);
    }

    private void bindingView(@NonNull View view) {
        emailText = (EditText) view.findViewById(R.id.emailTV);
        contButton = (Button) view.findViewById(R.id.contButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }
    private void setListener() {
        contButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.contButton:
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = emailText.getText().toString().trim();
                showToast(emailAddress);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Fragment secondFragment = new FogotPasswordSecondFragment();
                                    openNewFragment(secondFragment);
                                } else {
                                    showToast("Wrong Email");
                                }
                            }
                        });
                break;
        }
    }


    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment2")
                .addToBackStack(null)
                .commit();
    }
}
