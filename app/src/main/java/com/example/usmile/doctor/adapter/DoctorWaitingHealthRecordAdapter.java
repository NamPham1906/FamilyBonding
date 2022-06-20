package com.example.usmile.doctor.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.models.HealthRecord;

import java.util.List;

public class DoctorWaitingHealthRecordAdapter extends RecyclerView.Adapter<DoctorWaitingHealthRecordAdapter.DoctorWaitingHealthRecordViewHolder>{


    private List<HealthRecord> healthRecords;
    private Context context;

    public DoctorWaitingHealthRecordAdapter(List<HealthRecord> list) {
        this.healthRecords = list;
    }

    public void setData(List<HealthRecord> list) {
        this.healthRecords = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DoctorWaitingHealthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_waiting_health_record, parent, false);

        context = parent.getContext();

        return new DoctorWaitingHealthRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorWaitingHealthRecordViewHolder holder, int position) {
        HealthRecord item = healthRecords.get(position);

        if (item == null)
            return;


        holder.senderMessage.setText(item.getDescription());
        holder.sendDate.setText(item.getSentDate());

        // from account id -> load user avatar and name from firestore
        holder.senderAvatar.setImageResource(R.drawable.example_avatar);
        holder.senderName.setText("Nam 7749");

        holder.checkHealthRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Not implement yet");
            }
        });

        holder.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Not implement either");
            }
        });

    }

    private void showToast(String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        if (healthRecords == null)
            return 0;
        return healthRecords.size();
    }

    public class DoctorWaitingHealthRecordViewHolder extends RecyclerView.ViewHolder {

        ImageView senderAvatar;
        TextView senderName;
        TextView sendDate;
        TextView senderMessage;

        TextView checkHealthRecordButton;
        TextView skipButton;


        public DoctorWaitingHealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            senderAvatar = (ImageView) itemView.findViewById(R.id.senderAvatar);
            sendDate = (TextView) itemView.findViewById(R.id.sendDate);
            senderName = (TextView) itemView.findViewById(R.id.senderName);
            senderMessage = (TextView) itemView.findViewById(R.id.senderMessage);

            checkHealthRecordButton = (TextView) itemView.findViewById(R.id.checkHealthRecordButton);
            skipButton = (TextView) itemView.findViewById(R.id.skipButton);
        }
    }
}
