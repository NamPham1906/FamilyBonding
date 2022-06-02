package com.example.usmile.user.fragment;

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
import com.example.usmile.user.adapters.HealthRecordAdapter;
import com.example.usmile.user.adapters.TipsAdapter;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.user.models.Tips;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HealthRecordFragment extends Fragment {

    RecyclerView recordRecyclerView;
    List<HealthRecord> healthRecords;
    HealthRecordAdapter adapter;

    FloatingActionButton newHealthRecordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordRecyclerView = (RecyclerView) view.findViewById(R.id.recordRecyclerView);
        newHealthRecordButton = (FloatingActionButton) view.findViewById(R.id.newHealthRecordButton);

        initData();
        initRecordRecyclerView();
    }

    private void initRecordRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recordRecyclerView.setLayoutManager(layoutManager);
        adapter = new HealthRecordAdapter(healthRecords);
        recordRecyclerView.setAdapter(adapter);
        recordRecyclerView.setHasFixedSize(true);
    }

    private void initData() {
        healthRecords = new ArrayList<>();
        healthRecords.add(new HealthRecord(1,null,"",false,"Ngày 02/06/2022"));
        healthRecords.add(new HealthRecord(1,null,"",false,"Ngày 04/05/2022"));
        healthRecords.add(new HealthRecord(1,null,"",false,"Ngày 12/04/2022"));
    }
}