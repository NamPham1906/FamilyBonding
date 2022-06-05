package com.example.usmile.login;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.R;
import com.example.usmile.login.fragment.RegisterFragment;
import com.example.usmile.user.fragment.CollectPictureFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;
    Fragment fragment = null;
    String accountType = "";
    Button loginButton;
    Button registerButton;
    TextView fogotPasswordTextView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton =  this.findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        registerButton = this.findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(this);
        fogotPasswordTextView = this.findViewById(R.id.fogotPasswordTextView);
        fogotPasswordTextView.setOnClickListener(this);
        progressBar = this.findViewById(R.id.progressBar);




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                progressBar.setVisibility(View.VISIBLE);
                String email = "phamnam0126@gmail.com";
                String password = "pass123";
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

                break;
            case R.id.registerBtn:

                 fragmentManager = getSupportFragmentManager();
                 fragmentManager.beginTransaction().replace(R.id.loginFragmentHolder, RegisterFragment.class, null).commit();
                break;
            case R.id.forgetPasswordBtn:

                break;
        }
    }

    public void updateUI(FirebaseUser user){
        if (user!=null) {
            String email = user.getEmail();
            accountType = AccountFactory.USERSTRING;
            Intent intent = new Intent(getApplicationContext(), AccountFactory.createAccountClass(accountType));
            Account account = AccountFactory.createAccount(accountType, email);
            intent.putExtra(account.type(), account);
            startActivity(intent);
            this.finish();
        }
    }

}