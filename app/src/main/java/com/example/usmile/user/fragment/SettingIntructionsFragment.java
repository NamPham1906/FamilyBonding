package com.example.usmile.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.adapters.InstructionItemAdapter;
import com.example.usmile.user.models.InstructionItem;

import java.util.ArrayList;
import java.util.List;


public class SettingIntructionsFragment extends Fragment implements SearchView.OnQueryTextListener {


    RecyclerView instructionsRecyclerView;
    List<InstructionItem> instructionItemList;
    SearchView instructionSearchView;
    InstructionItemAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instructionsRecyclerView = (RecyclerView) view.findViewById(R.id.instructionItemRecyclerView);
        instructionSearchView = (SearchView) view.findViewById(R.id.instructionSearchView);

        instructionSearchView.setOnQueryTextListener(this);
        
        initData();
        setRecyclerView();
        
    }

    private void setRecyclerView() {

        adapter = new InstructionItemAdapter(instructionItemList);
        instructionsRecyclerView.setAdapter(adapter);
        instructionsRecyclerView.setHasFixedSize(true);
    }

    private void initData() {

        instructionItemList = new ArrayList<>();

        instructionItemList.add(new InstructionItem("Giới thiệu về ứng dụng USmile","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
        instructionItemList.add(new InstructionItem("Làm thế nào để thay đổi mật khẩu ?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
        instructionItemList.add(new InstructionItem("Làm sao để gửi bệnh án?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
        instructionItemList.add(new InstructionItem("Cách để tìm phòng khám gần nhất","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
        instructionItemList.add(new InstructionItem("Cách xem phản hồi của bác sĩ ?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
        instructionItemList.add(new InstructionItem("Cách xem tình trạng hồ sơ đã gửi?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_intructions, container, false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    private void filter(String newText) {

        List<InstructionItem> filteredlist = new ArrayList<>();

        for (InstructionItem item : instructionItemList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getQuestion().toLowerCase().contains(newText.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }
}