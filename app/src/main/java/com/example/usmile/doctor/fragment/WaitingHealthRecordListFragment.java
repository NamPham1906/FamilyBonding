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
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.doctor.adapter.DoctorWaitingHealthRecordAdapter;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

//        initFakeData();
        initData();
        initRecyclerView();
    }

    public void initData() {
        healthRecords = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_HEALTH_RECORD_ACCEPTED, false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            List<String> healthPictures = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_PICTURES);
                            List<String> advices = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_ADVICES);

                            String id = doc.getString(Constants.KEY_HEALTH_RECORD_ID);
                            String userID = doc.getString(Constants.KEY_ACCOUNT_ID);
                            String description = doc.getString(Constants.KEY_HEALTH_RECORD_DESCRIPTION);
                            String sendDate = doc.getString(Constants.KEY_HEALTH_RECORD_DATE);
                            Boolean deleted = doc.getBoolean(Constants.KEY_HEALTH_RECORD_DELETED);
                            Boolean accepted = doc.getBoolean(Constants.KEY_HEALTH_RECORD_ACCEPTED);
                            String dentistId = doc.getString(Constants.KEY_HEALTH_RECORD_DENTIST_ID);

                            healthRecords.add(new HealthRecord(id, userID, description,
                                    healthPictures, advices, accepted, deleted, sendDate, dentistId));


                        }
                        Toast.makeText(getContext(), "read db successed", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
//    public void initFakeData() {
//
//        healthRecords = new ArrayList<>();
//
//        HealthRecord fake = new HealthRecord();
//        fake.setSentDate("Ngày 20/06/2022");
//        fake.setDescription("Có phải cháu đang mọc răng ...");
//
//        for (int i = 0; i < 5; i++)
//            healthRecords.add(fake);
//
//    }

    public void initRecyclerView() {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        waitingHealthRecordRecyclerView.setLayoutManager(layoutManager);
        adapter = new DoctorWaitingHealthRecordAdapter(healthRecords);
        waitingHealthRecordRecyclerView.setAdapter(adapter);
        waitingHealthRecordRecyclerView.setHasFixedSize(true);

    }
}
