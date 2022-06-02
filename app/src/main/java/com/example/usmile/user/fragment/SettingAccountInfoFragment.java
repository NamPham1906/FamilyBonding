package com.example.usmile.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;

import org.w3c.dom.Text;


public class SettingAccountInfoFragment extends Fragment implements View.OnClickListener {

    TextView changePasswordTextView;
    TextView confirmButton;
    TextView cancelButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changePasswordTextView = (TextView) view.findViewById(R.id.changePasswordTextView);
        confirmButton = (TextView) view.findViewById(R.id.confirmButton);
        cancelButton = (TextView) view.findViewById(R.id.cancelButton);

        changePasswordTextView.setOnClickListener(this);
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