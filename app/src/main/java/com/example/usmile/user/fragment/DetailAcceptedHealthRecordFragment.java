package com.example.usmile.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usmile.R;


public class DetailAcceptedHealthRecordFragment extends Fragment {

    private TextView recordTimeTextView;
    private TextView doctorFullNameTextView;
    private TextView doctorWorkPlaceTextView;
    private EditText patientMessageEditText;

    ImageView firstPicture;
    ImageView secondPicture;
    ImageView thirdPicture;
    ImageView fourthPicture;

    TextView firstDetailAdvice;
    TextView secondDetailAdvice;
    TextView thirdDetailAdvice;
    TextView fourthDetailAdvice;


    public DetailAcceptedHealthRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_accepted_health_record, container, false);
    }
}