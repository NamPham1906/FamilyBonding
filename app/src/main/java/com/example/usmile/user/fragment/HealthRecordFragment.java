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
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
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
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_record, container, false);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        recordRecyclerView = (RecyclerView) view.findViewById(R.id.recordRecyclerView);
        newHealthRecordButton = (FloatingActionButton) view.findViewById(R.id.newHealthRecordButton);
        newHealthRecordButton.setOnClickListener(this);
        initDataForMultiAdapter();
        initRecordRecyclerViewMulti();
    }

    private void initRecordRecyclerViewMulti() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recordRecyclerView.setLayoutManager(layoutManager);
        multiAdapter = new MultiHealthRecordAdapter(healthRecords);
        multiAdapter.setUser(user);
        recordRecyclerView.setAdapter(multiAdapter);
        recordRecyclerView.setHasFixedSize(true);
    }


    private void initDataForMultiAdapter() {
        healthRecords = new ArrayList<>();
        String user_id = user.id();
        if (user==null) return;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_ACCOUNT_ID, user_id)
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
                        multiAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
}