package com.example.usmile.doctor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
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
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.account.models.User;
import com.example.usmile.doctor.adapter.DoctorAcceptedHealthRecordAdapter;
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
import java.util.function.Predicate;

public class ReceivedHealthRecordListFragment extends Fragment {

    RecyclerView receivedHeathRecordRecyclerView;
    List<HealthRecord> healthRecords;
    DoctorAcceptedHealthRecordAdapter adapter;

    Doctor doctor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_received_health_record, container, false);
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctor = (Doctor) bundle.getSerializable(AccountFactory.DOCTORSTRING);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        receivedHeathRecordRecyclerView = (RecyclerView) view.findViewById(R.id.receivedHealthRecordView);
        initData();
        initRecyclerView();
    }

    public void initData() {
        healthRecords = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_HEALTH_RECORD_DENTIST_ID,
                       doctor.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            List<String> healthPictures = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_PICTURES);
                            List<String> advices = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_ADVICES);
                            List<String> deleted = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_DELETED);

                            String id = doc.getString(Constants.KEY_HEALTH_RECORD_ID);
                            String userID = doc.getString(Constants.KEY_ACCOUNT_ID);
                            String description = doc.getString(Constants.KEY_HEALTH_RECORD_DESCRIPTION);
                            String sendDate = doc.getString(Constants.KEY_HEALTH_RECORD_DATE);
                            Boolean accepted = doc.getBoolean(Constants.KEY_HEALTH_RECORD_ACCEPTED);
                            String dentistId = doc.getString(Constants.KEY_HEALTH_RECORD_DENTIST_ID);

                            healthRecords.add(new HealthRecord(id, userID, description,
                                    healthPictures, advices, accepted, deleted, sendDate, dentistId));

                        }
                        Toast.makeText(getContext(), "read db successed", Toast.LENGTH_LONG).show();
                        for (int i = healthRecords.size() - 1; i >= 0; i--)
                        {
                            List<String> del = healthRecords.get(i).getDeleted();
                            if(del.isEmpty())
                                continue;
                            else
                            {
                                for(String str:del)
                                {
                                    //Log.d("delete id", str);
                                    if (str!=null) {
                                        if (str.equals(doctor.getId())) {
                                            healthRecords.remove(i);
                                        }
                                    }
                                }
                            }
                        }
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

    public void initRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        receivedHeathRecordRecyclerView.setLayoutManager(layoutManager);
        adapter = new DoctorAcceptedHealthRecordAdapter(healthRecords);
        adapter.setDoctor(doctor);
        receivedHeathRecordRecyclerView.setAdapter(adapter);
        receivedHeathRecordRecyclerView.setHasFixedSize(true);

    }


}