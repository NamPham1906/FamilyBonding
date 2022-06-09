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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.R;
import com.example.usmile.login.fragment.RegisterFirstFragment;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;
    Fragment fragment = null;
    String accountType = "";
    Button loginButton;
    Button registerButton;
    TextView forgotPasswordTextView;
    ProgressBar progressBar;

    EditText accountSignInTV;
    EditText passwordSignInTV;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            // assume this is user

            if (preferenceManager.getString(Constants.KEY_ACCOUNT_TYPE) == AccountFactory.USERSTRING) {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        loginButton =  this.findViewById(R.id.loginBtn);
        registerButton = this.findViewById(R.id.registerBtn);
        forgotPasswordTextView = this.findViewById(R.id.forgotPasswordTextView);
        progressBar = this.findViewById(R.id.progressBar);
        accountSignInTV = this.findViewById(R.id.accountSignInTV);
        passwordSignInTV = this.findViewById(R.id.passSignInTV);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);

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


                if (!isValidLoginDetails())
                    return;

                signIn();

                break;
            case R.id.registerBtn:

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.loginFragmentHolder, RegisterFirstFragment.class, null).commit();

                break;
            case R.id.forgotPasswordTextView:

                showToast("Oh hi cool kid ?");

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

    private Boolean isFilledDetail(EditText editText) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            editText.setError("Không được để trống");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    private void signIn() {

        String accountStr = accountSignInTV.getText().toString().trim();
        String password = passwordSignInTV.getText().toString().trim();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_ACCOUNT_ACCOUNT, accountStr)
                .whereEqualTo(Constants.KEY_ACCOUNT_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null
                        && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_ACCOUNT_ID, documentSnapshot.getId());

                        preferenceManager.putString(Constants.KEY_ACCOUNT_AVATAR, documentSnapshot.getString(Constants.KEY_ACCOUNT_AVATAR));
                        preferenceManager.putString(Constants.KEY_ACCOUNT_FULL_NAME, documentSnapshot.getString(Constants.KEY_ACCOUNT_FULL_NAME));
                        preferenceManager.putString(Constants.KEY_ACCOUNT_DOB, documentSnapshot.getString(Constants.KEY_ACCOUNT_DOB));
                        preferenceManager.putString(Constants.KEY_ACCOUNT_GENDER, documentSnapshot.getString(Constants.KEY_ACCOUNT_GENDER));

                        preferenceManager.putString(Constants.KEY_ACCOUNT_ACCOUNT, documentSnapshot.getString(Constants.KEY_ACCOUNT_ACCOUNT));
                        preferenceManager.putString(Constants.KEY_ACCOUNT_PASSWORD, documentSnapshot.getString(Constants.KEY_ACCOUNT_PASSWORD));
                        preferenceManager.putString(Constants.KEY_ACCOUNT_TYPE, documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE));


                        // assume type = "User"
                        Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        showToast("Unable to sign in");
                    }
                });
    }

    private Boolean isValidLoginDetails() {

        if (!isFilledDetail(accountSignInTV))
            return false;

        if (!isFilledDetail(passwordSignInTV))
            return false;

        return true;
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}