package com.example.usmile.user.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.adapters.NewTipsAdapter;
import com.example.usmile.user.adapters.TipsAdapter;
import com.example.usmile.user.models.Tips;
import com.example.usmile.utilities.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    List<Tips> allTips;
    List<Tips> news, tools, foods, latest;


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

    private void pullTipsFromDatabase() {


        FirebaseFirestore database = FirebaseFirestore.getInstance();

        allTips = new ArrayList<>();

        tools = new ArrayList<>();
        foods = new ArrayList<>();
        news = new ArrayList<>();
        latest = new ArrayList<>();

        String foodStr = "Thực phẩm";
        String newsStr = "Tin mới";
        String toolStr = "Dụng cụ nha khoa";

        database.collection(Constants.KEY_COLLECTION_TIP)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            Tips tip = documentSnapshot.toObject(Tips.class);
                            allTips.add(tip);

                            if (tip.getType().equals(foodStr))
                                foods.add(tip);
                            else if (tip.getType().equals(newsStr))
                                news.add(tip);
                            else if (tip.getType().equals(toolStr))
                                tools.add(tip);
                        }

                    }

                });


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

        // implant
        newTips.add(new Tips(R.drawable.implant,"Xu hướng công nghệ","https://tdental.vn/blog/xu-huong-cong-nghe-nha-khoa-hien-dai-t30260.html","Tin mới","Cùng TDental điểm qua một số công nghệ nha khoa hiện đại..."));
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


        // dental floss
        toolTips.add(new Tips(R.drawable.dental_floss,"Chỉ nha khoa và tăm nước","https://youmed.vn/tin-tuc/chi-nha-khoa-va-tam-nuoc-dung-cu-nao-tot-hon/","Dụng cụ nha khoa","Chăm sóc răng miệng, nhất là vùng kẽ, rất quan trọng..."));
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

        // healthy food
        foodTips.add(new Tips(R.drawable.healthy_food,"Thực phẩm cần thiết","https://thanhnien.vn/7-loai-thuc-pham-can-thiet-cho-rang-khoe-manh-post1451875.html","Thực phẩm","Đối với hầu hết mọi người, một thói quen sức khỏe răng miệng lành mạnh ..."));
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


        latestTips.add(new Tips(R.drawable.boy,"Răng sứ","https://www.edendental.vn/nha-khoa-tham-my/rang-su.html","Tin mới","Răng sứ là các phục hồi răng được làm bằng chất liệu sứ nhằm tái tạo ..."));
        latestTips.add(new Tips(R.drawable.healthy_1,"Ăn gì tốt cho răng","https://nhakhoaparis.vn/an-gi-tot-cho-rang.html","Thực phẩm","Bên cạnh việc vệ sinh răng miệng không đúng cách thì thói quen ăn..."));
        latestTips.add(new Tips(R.drawable.healthy_2,"Chế độ ăn uống","https://vov.vn/suc-khoe/thuc-pham-can-thiet-de-rang-khoe-manh-post941338.vov","Thực phẩm","VOV.VN - Nếu chế độ ăn uống không giàu chất dinh dưỡng thì bạn sẽ dễ mắc các ..."));
        latestTips.add(new Tips(R.drawable.otho,"Niềng răng Trainer","https://nhakhoatandinh.com/nieng-rang-trainer/","Dụng cụ nha khoa","Hiện nay có nhiều phương pháp chỉnh nha, thẩm mỹ răng miệng trong đó..."));

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

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private class NewTipViewHolder extends RecyclerView.ViewHolder {

        ImageView tipImageView;
        TextView titleTextView;
        TextView sourceTextView;
        TextView timeTextView;
        TextView checkDetailsButton;


        public NewTipViewHolder(@NonNull View itemView) {
            super(itemView);

            tipImageView = (ImageView) itemView.findViewById(R.id.tipImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            sourceTextView = (TextView) itemView.findViewById(R.id.sourceTextView);

            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            checkDetailsButton = (TextView) itemView.findViewById(R.id.checkDetailButton);
        }
    }
}