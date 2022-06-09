package com.example.usmile.user.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;

import org.w3c.dom.Text;


import android.util.Base64;


public class SettingAccountInfoFragment extends Fragment implements View.OnClickListener {

    TextView changePasswordTextView;
    TextView confirmButton;
    TextView cancelButton;

    ImageView avatarImageView;
    EditText userNameEditText;
    EditText fullNameEditText;
    EditText dobEditText;
    EditText genderEditText;
    EditText accountEditText;

    PreferenceManager preferenceManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        bindingView(view);

        loadAccountDetails();

        setListeners();
    }

    private void setListeners() {
        changePasswordTextView.setOnClickListener(this);
    }



    private void bindingView(@NonNull View view) {
        changePasswordTextView = (TextView) view.findViewById(R.id.changePasswordTextView);
        confirmButton = (TextView) view.findViewById(R.id.confirmButton);
        cancelButton = (TextView) view.findViewById(R.id.cancelButton);

        avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
        userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        fullNameEditText = (EditText) view.findViewById(R.id.fullNameEditText) ;
        dobEditText = (EditText) view.findViewById(R.id.dobEditText);
        genderEditText = (EditText) view.findViewById(R.id.genderEditText);
        accountEditText = (EditText) view.findViewById(R.id.accountEditText);
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void loadAccountDetails() {
        Bitmap bitmap = decodeImage(preferenceManager.getString(Constants.KEY_ACCOUNT_AVATAR));
        avatarImageView.setImageBitmap(bitmap);

        fullNameEditText.setText(preferenceManager.getString(Constants.KEY_ACCOUNT_FULL_NAME));
        dobEditText.setText(preferenceManager.getString(Constants.KEY_ACCOUNT_DOB));
        genderEditText.setText(preferenceManager.getString(Constants.KEY_ACCOUNT_GENDER));
        accountEditText.setText(preferenceManager.getString(Constants.KEY_ACCOUNT_ACCOUNT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_account_info, container, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Fragment nextFragment = null;

        switch (id) {
            case R.id.changePasswordTextView:
                nextFragment = new SettingChangePasswordFragment();
                break;
            case R.id.cancelButton:
                showToast("Cancel");
                break;
            case R.id.confirmButton:
                showToast("Confirm");
                break;
        }

        if (id == R.id.changePasswordTextView) {
            if (nextFragment != null)
                openNewFragment(nextFragment);
        }

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