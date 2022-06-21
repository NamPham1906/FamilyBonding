package com.example.usmile.doctor.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usmile.R;
import com.example.usmile.doctor.adapter.DoctorWaitingHealthRecordAdapter;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class WaitingHealthRecordListFragment extends Fragment {

    RecyclerView waitingHealthRecordRecyclerView;
    List<HealthRecord> healthRecords;
    DoctorWaitingHealthRecordAdapter adapter;

    PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting_health_record_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingHealthRecordRecyclerView = (RecyclerView) view.findViewById(R.id.waitingHealthRecordView);

        initFakeData();
        initRecyclerView();
    }

    public void initFakeData() {

        healthRecords = new ArrayList<>();

        HealthRecord fake = new HealthRecord();
        fake.setSentDate("Ngày 20/06/2022");
        fake.setDescription("Có phải cháu đang mọc răng ...");

        for (int i = 0; i < 5; i++)
            healthRecords.add(fake);

    }

    public void initRecyclerView() {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        waitingHealthRecordRecyclerView.setLayoutManager(layoutManager);
        adapter = new DoctorWaitingHealthRecordAdapter(healthRecords);
        waitingHealthRecordRecyclerView.setAdapter(adapter);
        waitingHealthRecordRecyclerView.setHasFixedSize(true);

    }
}
