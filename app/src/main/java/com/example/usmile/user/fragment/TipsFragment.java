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
import android.widget.ScrollView;

import com.example.usmile.R;
import com.example.usmile.user.adapters.NewTipsAdapter;
import com.example.usmile.user.adapters.TipsAdapter;
import com.example.usmile.user.models.Tips;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TipsFragment extends Fragment implements View.OnClickListener {

    RecyclerView latestTipsRecyclerView;
    RecyclerView foodTipsRecyclerView, toolTipsRecyclerView, newRecyclerView;
    List<Tips> latestTips;
    List<Tips> newTips, toolTips, foodTips;

    NewTipsAdapter latestTipsAdapter;
    TipsAdapter newTipsAdapter, toolTipsAdapter, foodTipsAdapter;

    ScrollView scrollView;
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        latestTipsRecyclerView = (RecyclerView) view.findViewById(R.id.newTipsView);
        foodTipsRecyclerView = (RecyclerView) view.findViewById(R.id.foodTipsRecyclerView);
        toolTipsRecyclerView = (RecyclerView) view.findViewById(R.id.toolTipsRecyclerView);
        newRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);

        floatingActionButton.setOnClickListener(this);

        initDataForLatestTips();
        initRecyclerViewForLatestTips();

        initDataForFoodTips();
        initRecyclerViewForFoodTips();

        initDataForToolTips();
        initRecyclerViewForToolTips();

        initDataForNewTips();
        initRecyclerViewForNewTips();
    }

    private void initRecyclerViewForNewTips() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        newRecyclerView.setLayoutManager(layoutManager);
        newTipsAdapter = new TipsAdapter(newTips);
        newRecyclerView.setAdapter(newTipsAdapter);
        newRecyclerView.setHasFixedSize(true);
    }

    private void initDataForNewTips() {
        newTips = new ArrayList<>();
        newTips.add(new Tips(1,"Tin mới","https://github.com/NamPham1906/USmile/tree", "Tin mới"));
    }

    private void initRecyclerViewForToolTips() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        toolTipsRecyclerView.setLayoutManager(layoutManager);
        toolTipsAdapter = new TipsAdapter(toolTips);
        toolTipsRecyclerView.setAdapter(toolTipsAdapter);
        toolTipsRecyclerView.setHasFixedSize(true);
    }

    private void initDataForToolTips() {
        toolTips = new ArrayList<>();
        toolTips.add(new Tips(1,"Dụng cụ nè","https://github.com/NamPham1906/USmile/tree", "Dụng cụ nha khoa"));
    }

    private void initRecyclerViewForFoodTips() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        foodTipsRecyclerView.setLayoutManager(layoutManager);
        foodTipsAdapter = new TipsAdapter(foodTips);
        foodTipsRecyclerView.setAdapter(foodTipsAdapter);
        foodTipsRecyclerView.setHasFixedSize(true);
    }

    private void initDataForFoodTips() {
        foodTips = new ArrayList<>();
        foodTips.add(new Tips(1,"Thực phẩm nè","https://github.com/NamPham1906/USmile/tree", "Thực phẩm"));
    }

    private void initRecyclerViewForLatestTips() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        latestTipsRecyclerView.setLayoutManager(layoutManager);
        latestTipsAdapter = new NewTipsAdapter(latestTips);
        latestTipsRecyclerView.setAdapter(latestTipsAdapter);
        latestTipsRecyclerView.setHasFixedSize(true);
    }

    private void initDataForLatestTips() {

        latestTips = new ArrayList<>();
        latestTips.add(new Tips(1,"Hehehe","https://github.com/NamPham1906/USmile/tree", "Tin mới"));
        latestTips.add(new Tips(1,"Hehehe","https://github.com/NamPham1906/USmile/tree/nam","Dụng cụ nha khoa"));
        latestTips.add(new Tips(1,"Hehehe","https://github.com/NamPham1906/USmile/tree/nam","Dụng cụ nha khoa"));
        latestTips.add(new Tips(1,"Hehehe","https://github.com/NamPham1906/USmile/tree/nam","Thực phẩm"));
        latestTips.add(new Tips(1,"Hehehe","https://github.com/NamPham1906/USmile/tree/nam","Thực phẩm"));

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.floatingButton:
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                break;
        }
    }
}