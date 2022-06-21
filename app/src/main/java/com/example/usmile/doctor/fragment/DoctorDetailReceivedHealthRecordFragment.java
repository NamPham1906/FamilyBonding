package com.example.usmile.doctor.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usmile.R;

public class DoctorDetailReceivedHealthRecordFragment extends Fragment implements View.OnClickListener{


    ImageView firstPicture;
    ImageView secondPicure;
    ImageView thirdPicture;
    ImageView fourthPicuture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_detail_received_health_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstPicture = (ImageView) view.findViewById(R.id.firstDetailPicture);
        secondPicure = (ImageView) view.findViewById(R.id.secondDetailPicture);
        thirdPicture = (ImageView) view.findViewById(R.id.thirdDetailPicture);
        fourthPicuture = (ImageView) view.findViewById(R.id.fourthDetailPicture);

        firstPicture.setOnClickListener(this);
        secondPicure.setOnClickListener(this);
        thirdPicture.setOnClickListener(this);
        fourthPicuture.setOnClickListener(this);


    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void openNewFragment(View view, Fragment nextFragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = new DoctorGiveSpecificAdviceFragment();

        switch (view.getId()) {
            case R.id.firstDetailPicture:
                showToast("First");
                break;
            case R.id.secondDetailPicture:
                showToast("Second");
                break;
            case R.id.thirdDetailPicture:
                showToast("Third");
                break;
            case R.id.fourthDetailPicture:
                showToast("Fourth");
                break;
        }

        openNewFragment(view, fragment);

    }
}