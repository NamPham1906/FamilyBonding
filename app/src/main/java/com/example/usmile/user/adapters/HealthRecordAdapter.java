package com.example.usmile.user.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.models.HealthRecord;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.HealthRecordViewHolder>{

    private List<HealthRecord> healthRecords;
    private Context context;

    public HealthRecordAdapter(List<HealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    public HealthRecordAdapter() {
        this.healthRecords = new ArrayList<>();
    }




    @NonNull
    @Override
    public HealthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_record_accepted_item_layout, parent, false);

        context = parent.getContext();

        return new HealthRecordViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HealthRecordViewHolder holder, int position) {

        HealthRecord item = healthRecords.get(position);

        if (item == null)
            return;

        holder.dateTimeTextView.setText(item.getSentDate());

        // should add this property to Health Record ?
        holder.statusDetailTextView.setText("BS Nguyễn Tấn Hưng đã tiếp nhận");
    }


    @Override
    public int getItemCount() {
        if (healthRecords == null)
            return 0;
        return healthRecords.size();
    }

    public class HealthRecordViewHolder extends RecyclerView.ViewHolder {


        TextView dateTimeTextView;
        TextView statusDetailTextView;
        TextView leftButton;
        TextView rightButton;


        public HealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTimeTextView = (TextView) itemView.findViewById(R.id.dateTimeTextView);
            statusDetailTextView = (TextView) itemView.findViewById(R.id.statusDetailTextView);
            leftButton = (TextView) itemView.findViewById(R.id.leftButton);
            rightButton = (TextView) itemView.findViewById(R.id.rightButton);
        }
    }
}
