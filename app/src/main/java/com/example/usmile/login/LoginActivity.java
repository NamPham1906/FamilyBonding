package com.example.usmile.login;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.R;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.login.fragment.FogotPasswordFirstFragment;
import com.example.usmile.login.fragment.FogotPasswordSecondFragment;
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
import com.google.firebase.firestore.QuerySnapshot;

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
        /*preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            // assume this is user

            if (preferenceManager.getString(Constants.KEY_ACCOUNT_TYPE) == AccountFactory.USERSTRING) {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(intent);
                finish();
            }

        }*/

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

    private Account setInformation(DocumentSnapshot documentSnapshot){
        Account newAccount = AccountFactory.createAccount(documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE));
        newAccount.setAvatar(documentSnapshot.getString(Constants.KEY_ACCOUNT_AVATAR));
        newAccount.setFullName(documentSnapshot.getString(Constants.KEY_ACCOUNT_FULL_NAME));
        newAccount.setDOB(documentSnapshot.getString(Constants.KEY_ACCOUNT_DOB));
        newAccount.setGender(documentSnapshot.getString(Constants.KEY_ACCOUNT_GENDER));
        newAccount.setAccount(documentSnapshot.getString(Constants.KEY_ACCOUNT_ACCOUNT));
        if (documentSnapshot.get(Constants.KEY_ACCOUNT_TYPE).equals(AccountFactory.DOCTORSTRING)) {
            ((Doctor) newAccount).setWorkPlace( documentSnapshot.getString(Constants.KEY_ACCOUNT_WORKPLACE));
        }
        return newAccount;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            String email = currentUser.getEmail();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            progressBar.setVisibility(View.VISIBLE);
            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                    .whereEqualTo(Constants.KEY_ACCOUNT_EMAIL, email)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            updateUI(currentUser, setInformation(documentSnapshot));
                        }});
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if (!isValidLoginDetails())
                    return;
                signInAuth();
                break;
            case R.id.registerBtn:

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.loginFragmentHolder, RegisterFirstFragment.class, null).commit();
                break;
            case R.id.forgotPasswordTextView:
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.loginFragmentHolder, FogotPasswordFirstFragment.class, null).commit();
                break;
        }
    }

    public void updateUI(FirebaseUser user, Account account){

        if (user!=null) {
            Intent intent = new Intent(getApplicationContext(), AccountFactory.createAccountClass(account.type()));
            intent.putExtra(account.type(), account);
            startActivity(intent);
            this.finish();
        }
        else{
            showToast("user is null");
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

    private void signInAuth() {
        String accountStr = accountSignInTV.getText().toString().trim();
        String password = passwordSignInTV.getText().toString().trim();


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_ACCOUNT_ACCOUNT, accountStr)
                .get()
                .addOnCompleteListener(task -> {

                            if (task.isSuccessful() && task.getResult() != null
                                    && task.getResult().getDocuments().size() > 0) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String email = documentSnapshot.getString(Constants.KEY_ACCOUNT_EMAIL);

                                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        if (task.isSuccessful()){
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user, setInformation(documentSnapshot));
                                        } else{
                                            showToast("Wrong password");
                                        }

                                    }
                                });
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                showToast("Wrong user name");
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

// ....
                                           /* preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_ID, documentSnapshot.getId());

                                            preferenceManager.putString(Constants.KEY_ACCOUNT_AVATAR, documentSnapshot.getString(Constants.KEY_ACCOUNT_AVATAR));
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_FULL_NAME, documentSnapshot.getString(Constants.KEY_ACCOUNT_FULL_NAME));
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_DOB, documentSnapshot.getString(Constants.KEY_ACCOUNT_DOB));
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_GENDER, documentSnapshot.getString(Constants.KEY_ACCOUNT_GENDER));

                                            preferenceManager.putString(Constants.KEY_ACCOUNT_ACCOUNT, documentSnapshot.getString(Constants.KEY_ACCOUNT_ACCOUNT));
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_PASSWORD, documentSnapshot.getString(Constants.KEY_ACCOUNT_PASSWORD));
                                            preferenceManager.putString(Constants.KEY_ACCOUNT_TYPE, documentSnapshot.getString(Constants.KEY_ACCOUNT_TYPE));

                                            if (documentSnapshot.get(Constants.KEY_ACCOUNT_TYPE).equals(AccountFactory.DOCTORSTRING)) {
                                                preferenceManager.putString(Constants.KEY_ACCOUNT_WORKPLACE, documentSnapshot.getString(Constants.KEY_ACCOUNT_WORKPLACE));
                                            }
                                            //if (documentSnapshot.getString())
                                            // ....*/