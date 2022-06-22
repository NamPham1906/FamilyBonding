package com.example.usmile.user.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;


import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;


public class SettingAccountInfoFragment extends Fragment implements View.OnClickListener {

    TextView changePasswordTextView;
    TextView confirmButton;
    TextView cancelButton;

    //ImageView avatarImageView;
    RoundedImageView avatarImageView;
    EditText userNameEditText;
    EditText fullNameEditText;
    EditText dobEditText;
    EditText genderEditText;
    EditText accountEditText;

    PreferenceManager preferenceManager;

    String encodedImage = "";

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_account_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        getBundle();

        bindingView(view);

        loadAccountDetails();

        setListeners();
    }

    private void setListeners() {
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        changePasswordTextView.setOnClickListener(this);
        avatarImageView.setOnClickListener(this);
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null)
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);
    }




    private void bindingView(@NonNull View view) {
        changePasswordTextView = (TextView) view.findViewById(R.id.changePasswordTextView);
        confirmButton = (TextView) view.findViewById(R.id.confirmButton);
        cancelButton = (TextView) view.findViewById(R.id.cancelButton);

        //avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
        avatarImageView = (RoundedImageView) view.findViewById(R.id.avatarImageView);

        userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        fullNameEditText = (EditText) view.findViewById(R.id.fullNameEditText) ;
        dobEditText = (EditText) view.findViewById(R.id.dobEditText);
        genderEditText = (EditText) view.findViewById(R.id.genderEditText);
        accountEditText = (EditText) view.findViewById(R.id.accountEditText);
    }

    private Bitmap decodeImage(String encodedImage) {
        if (encodedImage == null) return null;
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void loadAccountDetails() {
        Bitmap bitmap = decodeImage(user.getAvatar());
        avatarImageView.setImageBitmap(bitmap);


        fullNameEditText.setText(user.getFullName());
        dobEditText.setText(user.getDOB());
        genderEditText.setText(user.getGender());
        accountEditText.setText(user.getAccount());
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();

        Fragment nextFragment = null;

        switch (id) {
            case R.id.changePasswordTextView:
                Bundle bundle = new Bundle();
                bundle.putString("TYPE", AccountFactory.USERSTRING);

                bundle.putSerializable(AccountFactory.USERSTRING, user);

                nextFragment = new SettingChangePasswordFragment();
                nextFragment.setArguments(bundle);

                break;
            case R.id.cancelButton:
                showToast("Cancel");
                break;
            case R.id.confirmButton:
                updateInfo();
                break;
            case R.id.avatarImageView:
                selectImage();
                break;
        }

        if (id == R.id.changePasswordTextView) {
            if (nextFragment != null)
                openNewFragment(nextFragment);
        }
    }

    private void updateInfo() {

        String fullname = fullNameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String accountStr = accountEditText.getText().toString();


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(preferenceManager.getString(Constants.KEY_ACCOUNT_ID));

        HashMap<String, Object> updates = new HashMap<>();

        if (!encodedImage.equals(""))
            updates.put(Constants.KEY_ACCOUNT_AVATAR, encodedImage);

        updates.put(Constants.KEY_ACCOUNT_FULL_NAME, fullname);
        updates.put(Constants.KEY_ACCOUNT_DOB, dob);
        updates.put(Constants.KEY_ACCOUNT_GENDER, gender);
        updates.put(Constants.KEY_ACCOUNT_ACCOUNT, accountStr);

        documentReference.update(updates)
                .addOnSuccessListener(unused -> {

                    if (!encodedImage.equals(""))
                        preferenceManager.putString(Constants.KEY_ACCOUNT_AVATAR, encodedImage);

                    preferenceManager.putString(Constants.KEY_ACCOUNT_FULL_NAME, fullname);
                    preferenceManager.putString(Constants.KEY_ACCOUNT_DOB, dob);
                    preferenceManager.putString(Constants.KEY_ACCOUNT_GENDER, gender);
                    preferenceManager.putString(Constants.KEY_ACCOUNT_ACCOUNT, accountStr);

                    showToast("Updated successfully");

                    Intent intent = new Intent(getContext(), UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    showToast("Unable to update");
                });
    }

    private void selectImage() {
        // open gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        // handle everything after picking
        pickImage.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {

                            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatarImageView.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}