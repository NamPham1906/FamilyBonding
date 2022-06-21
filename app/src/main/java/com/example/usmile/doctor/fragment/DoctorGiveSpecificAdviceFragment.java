package com.example.usmile.doctor.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usmile.R;
import com.example.usmile.doctor.DoctorMainActivity;
import com.example.usmile.utilities.PreferenceManager;

public class DoctorGiveSpecificAdviceFragment extends Fragment implements View.OnClickListener{

    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;

    EditText firstDetailAdvice;
    EditText secondDetailAdvice;
    EditText thirdDetailAdvice;
    EditText fourthDetailAdvice;
    EditText doctorRequirementTextView;

    TextView sendButton;

    PreferenceManager preferenceManager;

    DoctorMainActivity main;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_give_specific_advice, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(getContext());

        main = (DoctorMainActivity) getActivity();

        firstDetailAdvice = (EditText) view.findViewById(R.id.firstDetailAdvice);
        secondDetailAdvice = (EditText) view.findViewById(R.id.secondDetailAdvice);
        thirdDetailAdvice = (EditText) view.findViewById(R.id.thirdDetailAdvice);
        fourthDetailAdvice = (EditText) view.findViewById(R.id.fourthDetailAdvice);
        doctorRequirementTextView = (EditText) view.findViewById(R.id.doctorRequirementTextView);

        firstImageView = (ImageView) view.findViewById(R.id.patientFirstDetailPicture);
        secondImageView = (ImageView) view.findViewById(R.id.patientSecondDetailPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.patientThirdDetailPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.patientFourthDetailPicture);

        sendButton = (TextView) view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        loadHealthRecordDetails();

    }

    private void loadHealthRecordDetails() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sendButton:
                break;
        }
    }



}