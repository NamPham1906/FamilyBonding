package com.example.usmile.user.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.user.adapters.HealthRecordAdapter;
import com.example.usmile.user.adapters.MultiHealthRecordAdapter;
import com.example.usmile.user.adapters.TipsAdapter;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.user.models.Tips;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HealthRecordFragment extends Fragment implements View.OnClickListener {

    RecyclerView recordRecyclerView;
    List<HealthRecord> healthRecords = new ArrayList<>();
    HealthRecordAdapter adapter;
    MultiHealthRecordAdapter multiAdapter;

    FloatingActionButton newHealthRecordButton;

    PreferenceManager preferenceManager;

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

        newHealthRecordButton.setOnClickListener(this);

        preferenceManager = new PreferenceManager(getContext());


        //initData();
        //initRecordRecyclerView();

        initDataForMultiAdapter();
        initRecordRecyclerViewMulti();
    }

    private void initRecordRecyclerViewMulti() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recordRecyclerView.setLayoutManager(layoutManager);
        multiAdapter = new MultiHealthRecordAdapter(healthRecords);
        recordRecyclerView.setAdapter(multiAdapter);
        recordRecyclerView.setHasFixedSize(true);
    }

    private void initDataForMultiAdapter() {

        String user_id = preferenceManager.getString(Constants.KEY_ACCOUNT_ID);
        Log.d("userid: ", user_id );

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_ACCOUNT_ID, user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            List<String> healthPictures = (ArrayList) doc.get("healthPictures");
                            String userID = doc.getString(Constants.KEY_ACCOUNT_ID);
                            String description = doc.getString("description");
                            String advice = doc.getString("advices");
                            String sendDate = doc.getString("sendDate");
                            Boolean deleted = doc.getBoolean("deleted");
                            Boolean accepted = doc.getBoolean("accepted");
                            healthRecords.add(new HealthRecord(userID, description,
                                            healthPictures, advice, accepted, deleted, sendDate));
                            //hr.setHealthPictures(healthPictures);
                            //healthRecords.add(hr);
                            Log.d("userid",userID );
                            Log.d("description",description );
                            Log.d("accepted", accepted.toString());

                        }
                        Toast.makeText(getContext(), "read db successed", Toast.LENGTH_LONG).show();
                        multiAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
//        healthRecords.add(new HealthRecord("1","",null,"",false,false,"Ngày 02/06/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",true,false,"Ngày 04/05/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",true,false,"Ngày 12/04/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",false,false,"Ngày 03/04/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",true,false,"Ngày 02/02/2022"));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.newHealthRecordButton:
                Fragment newHealthRecordFragment = new CollectPictureFragment();
                openNewFragment(newHealthRecordFragment);
                break;
        }
    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

//    private void initRecordRecyclerView() {
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
//        recordRecyclerView.setLayoutManager(layoutManager);
//        adapter = new HealthRecordAdapter(healthRecords);
//        recordRecyclerView.setAdapter(adapter);
//        recordRecyclerView.setHasFixedSize(true);
//    }
//
//    private void initData() {
//        healthRecords = new ArrayList<>();
//        healthRecords.add(new HealthRecord("1","",null,"",false,"Ngày 02/06/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",false,"Ngày 04/05/2022"));
//        healthRecords.add(new HealthRecord("1","",null,"",false,"Ngày 12/04/2022"));
//    }
}