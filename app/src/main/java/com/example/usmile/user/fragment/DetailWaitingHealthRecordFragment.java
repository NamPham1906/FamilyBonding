package com.example.usmile.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usmile.R;
import com.example.usmile.user.models.HealthRecord;

public class DetailWaitingHealthRecordFragment extends Fragment {

    public DetailWaitingHealthRecordFragment(HealthRecord healthRecord) {

    }

    public DetailWaitingHealthRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_waiting_health_record, container, false);
    }
}