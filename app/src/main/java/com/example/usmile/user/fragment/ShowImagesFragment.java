package com.example.usmile.user.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.adapters.NewTipsAdapter;
import com.example.usmile.user.adapters.ShowImagesAdapter;
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

import io.grpc.internal.DnsNameResolver;

public class ShowImagesFragment extends Fragment {

    RecyclerView imageRecyclerView;

    PreferenceManager preferenceManager;
    List<String> imagesList;
    String sentDate;
    String sentMessage;
    ShowImagesAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_pictures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.showImageView);

        initDataForShowImages();
        Log.d("1003 frag after init", String.valueOf(imagesList.size()) + " - " + sentDate + " - " + sentMessage );
        initRecyclerViewForShowImages();

    }

    private void initDataForShowImages() {

        imagesList = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String healthRecordId = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID);
//        Log.d("1004", healthRecordId);

        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_HEALTH_RECORD_ID, healthRecordId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                        List<String> healthPictures = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_PICTURES);
                        if(healthPictures != null)
                            imagesList.addAll(healthPictures);
                        sentDate = doc.getString(Constants.KEY_HEALTH_RECORD_DATE);
                        sentMessage = doc.getString(Constants.KEY_HEALTH_RECORD_DESCRIPTION);
                        Log.d("1002 frag initing", String.valueOf(imagesList.size()) + " - " + sentDate + " - " + sentMessage );

                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage());
                    }
                });


    }

    private void initRecyclerViewForShowImages() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        Log.d("1005 nhét data vào adap", String.valueOf(imagesList.size()) + " - " + sentDate + " - " + sentMessage );

        adapter = new ShowImagesAdapter(imagesList, sentDate, sentMessage);
        Log.d("1007 check adap", adapter.getDate() + " - " + String.valueOf(adapter.getImg()));
        imageRecyclerView.setAdapter(adapter);
        imageRecyclerView.setHasFixedSize(true);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}