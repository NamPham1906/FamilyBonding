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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.doctor.fragment.DoctorDetailWaitingHealthRecordFragment;
import com.example.usmile.user.fragment.DetailWaitingHealthRecordFragment;
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



    public class DoctorWaitingHealthRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            skipButton.setOnClickListener(this);
            checkHealthRecordButton.setOnClickListener(this);
        }

        private void openNewFragment(View view, Fragment nextFragment) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
        }

        @Override
        public void onClick(View view) {

            int id = view.getId();
            int position = getLayoutPosition();
            HealthRecord item = healthRecords.get(position);

            switch (id) {
                case R.id.checkHealthRecordButton:

                    Fragment fragment = new DoctorDetailWaitingHealthRecordFragment();
                    openNewFragment(view, fragment);


                    break;
                case R.id.skipButton:
                    break;
            }

        }
    }
}
