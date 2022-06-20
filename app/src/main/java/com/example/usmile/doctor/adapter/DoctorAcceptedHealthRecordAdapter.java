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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.doctor.fragment.DoctorDetailReceivedHealthRecordFragment;
import com.example.usmile.doctor.fragment.DoctorDetailWaitingHealthRecordFragment;
import com.example.usmile.doctor.fragment.DoctorGiveSpecificAdviceFragment;
import com.example.usmile.user.models.HealthRecord;

import java.util.List;


public class DoctorAcceptedHealthRecordAdapter extends RecyclerView.Adapter<DoctorAcceptedHealthRecordAdapter.DoctorAcceptedHealthRecordViewHolder>{

    private List<HealthRecord> healthRecords;
    private Context context;

    public DoctorAcceptedHealthRecordAdapter(List<HealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    public void setData(List<HealthRecord> list) {
        this.healthRecords = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DoctorAcceptedHealthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_accepted_health_record, parent, false);
        context = parent.getContext();
        return new DoctorAcceptedHealthRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAcceptedHealthRecordViewHolder holder, int position) {
        HealthRecord item = healthRecords.get(position);

        if (item == null)
            return;

        holder.patientMessage.setText(item.getDescription());
        holder.patientSendDate.setText(item.getSentDate());

        // from account id -> load user avatar and name from firestore
        holder.patientAvatar.setImageResource(R.drawable.example_avatar);
        holder.patientName.setText("Nam 7749");

        if (item.isAdvised() == true) {
            holder.statusTextView.setText("Đã tư vấn");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_green));
        } else if (!item.isAdvised()) {
            holder.statusTextView.setText("Chờ tư vấn");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_yellow));
        }

    }

    @Override
    public int getItemCount() {
        if (healthRecords == null)
            return 0;
        return healthRecords.size();
    }

    private void showToast(String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public class DoctorAcceptedHealthRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView patientAvatar;
        TextView patientName;
        TextView patientSendDate;
        TextView statusTextView;
        TextView patientMessage;

        TextView checkDetailHealthRecordButton;
        TextView archiveButton;



        public DoctorAcceptedHealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            patientAvatar = (ImageView) itemView.findViewById(R.id.patientAvatar);
            patientName = (TextView) itemView.findViewById(R.id.patientName);
            patientSendDate = (TextView) itemView.findViewById(R.id.patientSendDate);
            statusTextView = (TextView) itemView.findViewById(R.id.statusTextView);
            patientMessage = (TextView) itemView.findViewById(R.id.patientMessage);



            checkDetailHealthRecordButton = (TextView) itemView.findViewById(R.id.checkDetailHealthRecordButton);
            archiveButton = (TextView) itemView.findViewById(R.id.archiveButton);


            checkDetailHealthRecordButton.setOnClickListener(this);
            archiveButton.setOnClickListener(this);

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

            Fragment fragment = null;

            switch (id) {
                case R.id.checkDetailHealthRecordButton:
                    fragment = new DoctorDetailReceivedHealthRecordFragment();
                    openNewFragment(view, fragment);
                    break;
                case R.id.archiveButton:
                    break;
            }
        }
    }
}
