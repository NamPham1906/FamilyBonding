package com.example.usmile.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.Account;
import com.example.usmile.account.models.User;
import com.example.usmile.user.adapters.GenderSpinnerAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterSecondFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinner;
    GenderSpinnerAdapter spinnerAdapter;

    Button contButton;

    EditText fullNameText;
    EditText emailText;
    EditText dobText;
    EditText phoneText;
    FrameLayout layoutImage;
    RoundedImageView imageProfile;
    TextView textAddImage;

    Account account;

    private String encodedImage = "";
    private String gender = getGenderList().get(0);


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getBundle();

        bindingView(view);

        initSpinner();

        setListener();
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        if (bundle != null)
            account = (Account) bundle.getSerializable("NEW_ACCOUNT");

        String msg = "Account type: " + account.type();
        if (account != null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



    private void initSpinner() {
        spinnerAdapter = new GenderSpinnerAdapter(getContext(), R.layout.item_gender_selected, getGenderList());
        spinnerAdapter.setDropDownViewResource(R.layout.item_gender_list);
        spinner.setAdapter(spinnerAdapter);
    }

    private void setListener() {
        spinner.setOnItemSelectedListener(this);
        contButton.setOnClickListener(this);
        layoutImage.setOnClickListener(this);
    }

    private void bindingView(@NonNull View view) {
        contButton = (Button) view.findViewById(R.id.contButton);
        spinner = (Spinner) view.findViewById(R.id.genderSpinner);
        fullNameText = (EditText) view.findViewById(R.id.fullNameText);
        dobText = (EditText) view.findViewById(R.id.dobText);
        phoneText = (EditText) view.findViewById(R.id.phoneText);
        emailText = (EditText) view.findViewById(R.id.emailText);
        layoutImage = (FrameLayout) view.findViewById(R.id.layoutImage);
        imageProfile = (RoundedImageView) view.findViewById(R.id.imageProfile);
        textAddImage = (TextView) view.findViewById(R.id.textAddImage);
    }

    private List<String> getGenderList() {
        List<String> list = new ArrayList<>();
        list.add("Nam");
        list.add("Nữ");
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_second, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getContext(), spinnerAdapter.getItem(i), Toast.LENGTH_SHORT).show();
        gender = spinnerAdapter.getItem(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.contButton:

                // check if user filled all the field

                if (!isCompleted())
                    return;

                Bundle bundle = new Bundle();
                bundle.putSerializable("NEW_ACCOUNT", account);

                Fragment thirdFragment = new RegisterThirdFragment();
                thirdFragment.setArguments(bundle);

                openNewFragment(thirdFragment);

                break;
            case R.id.layoutImage:
                selectImage();
                break;
        }
    }

    private boolean isCompleted() {

        if (encodedImage.equals("")) {
            Toast.makeText(getContext(), "Hãy chọn hình đại diện", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isFillEditText(fullNameText))
            return false;
        if (!isFillEditText(dobText))
            return false;
        if (!isFillEditText(phoneText))
            return false;
        if (!isFillEditText(emailText))
            return false;

        // waiting
        String fullname = fullNameText.getText().toString().trim();
        String dob = dobText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String email = emailText.getText().toString().trim();

        account.setFullName(fullname);
        account.setDOB(dob);
        account.setPhone(phone);
        account.setGender(gender);
        account.setAvatar(encodedImage);
        account.setEmail(email);

        return true;
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
                            imageProfile.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
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
}