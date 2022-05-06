package com.example.usmile.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;


public class SettingGeneralFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat themeSwitch;
    SwitchCompat notificationSwitch;

    TextView themeTextView;
    TextView notificationTextView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        themeSwitch = (SwitchCompat) view.findViewById(R.id.themeSwitch);
        notificationSwitch = (SwitchCompat) view.findViewById(R.id.notificationSwitch);

        themeSwitch.setChecked(true);
        notificationSwitch.setChecked(true);

        themeSwitch.setOnCheckedChangeListener(this);
        notificationSwitch.setOnCheckedChangeListener(this);

        themeTextView = (TextView) view.findViewById(R.id.themeTextView);
        notificationTextView = (TextView) view.findViewById(R.id.notificationTextView);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_general, container, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean on) {

        int id = compoundButton.getId();

        switch (id) {
            case R.id.themeSwitch:

                if (on)
                    themeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.light_mode, 0, 0, 0);
                else
                    themeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dark_mode,0,0,0);
                break;
            case R.id.notificationSwitch:

                if (on)
                    notificationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.noti_on, 0, 0, 0);
                else
                    notificationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.noti_off,0,0,0);
                break;
        }
    }
}