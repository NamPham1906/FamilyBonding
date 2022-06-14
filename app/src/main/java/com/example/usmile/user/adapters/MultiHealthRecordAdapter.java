package com.example.usmile.user.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.fragment.DetailAcceptedHealthRecordFragment;
import com.example.usmile.user.fragment.DetailWaitingHealthRecordFragment;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MultiHealthRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int WAITING_HEALTH_RECORD = 0;
    private static int ACCEPTED_HEALTH_RECORD = 1;

    private List<HealthRecord> healthRecords;
    private Context context;



    public MultiHealthRecordAdapter(List<HealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    public MultiHealthRecordAdapter() {
        this.healthRecords = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (ACCEPTED_HEALTH_RECORD == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_record_accepted_item_layout, parent, false);

            context = parent.getContext();

            return new AcceptedHealthRecordViewHolder(view);

        } else if (WAITING_HEALTH_RECORD == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_record_waiting_item_layout, parent, false);

            context = parent.getContext();

            return new WaitingHealthRecordViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HealthRecord item = healthRecords.get(position);

        if (item == null)
            return;

        if (ACCEPTED_HEALTH_RECORD == holder.getItemViewType()) {
            AcceptedHealthRecordViewHolder acceptedHealthRecordViewHolder = (AcceptedHealthRecordViewHolder) holder;

            acceptedHealthRecordViewHolder.dateTimeTextView.setText(item.getSentDate());

            // should add this property to Health Record ?
            acceptedHealthRecordViewHolder.statusDetailTextView.setText("BS Nguyễn Tấn Hưng đã tiếp nhận");
        } else if (WAITING_HEALTH_RECORD == holder.getItemViewType()) {

            WaitingHealthRecordViewHolder waitingHealthRecordViewHolder = (WaitingHealthRecordViewHolder) holder;

            waitingHealthRecordViewHolder.sentDateTextView.setText(item.getSentDate());
        }

    }

    @Override
    public int getItemCount() {
        if (healthRecords == null)
            return 0;
        return healthRecords.size();
    }

    @Override
    public int getItemViewType(int position) {
        HealthRecord item = healthRecords.get(position);

        if (true == item.isAccepted())
            return ACCEPTED_HEALTH_RECORD;
        else
            return WAITING_HEALTH_RECORD;
    }

    public class AcceptedHealthRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTimeTextView;
        TextView statusDetailTextView;
        TextView checkPictureButton;
        TextView checkAdviceButton;

        PreferenceManager preferenceManager;

        public AcceptedHealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTimeTextView = (TextView) itemView.findViewById(R.id.dateTimeTextView);
            statusDetailTextView = (TextView) itemView.findViewById(R.id.statusDetailTextView);
            checkPictureButton = (TextView) itemView.findViewById(R.id.checkPicturesButton);
            checkAdviceButton = (TextView) itemView.findViewById(R.id.checkAdvicesButton);

            checkAdviceButton.setOnClickListener(this);
            checkPictureButton.setOnClickListener(this);

            preferenceManager = new PreferenceManager(context);
        }



        @Override
        public void onClick(View view) {

            int id = view.getId();
            int position = getLayoutPosition();
            HealthRecord item = healthRecords.get(position);

            // pass object from fragment to fragment

            switch (id) {
                case R.id.checkAdvicesButton:
                    // this one first
                    preferenceManager.putString(Constants.KEY_HEALTH_RECORD_ID, item.getId());
                    Fragment accepted = new DetailAcceptedHealthRecordFragment();
                    openNewFragment(view, accepted);

                    break;
                case R.id.checkPicturesButton:
                    break;
            }
        }


        private void openNewFragment(View view, Fragment nextFragment) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
        }
    }

    public class WaitingHealthRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView sentDateTextView;
        TextView editButton;
        TextView cancelButton;
        Context context;

        PreferenceManager preferenceManager;



        public WaitingHealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            // binding view
            sentDateTextView = (TextView) itemView.findViewById(R.id.sentDateTextView);
            editButton = (TextView) itemView.findViewById(R.id.editButton);
            cancelButton = (TextView) itemView.findViewById(R.id.cancelButton);

            // click listener
            editButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            context = itemView.getContext();
            preferenceManager = new PreferenceManager(context);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getLayoutPosition();
            HealthRecord item = healthRecords.get(position);

            switch (id) {
                case R.id.editButton:
                    preferenceManager.putString(Constants.KEY_HEALTH_RECORD_ID, item.getId());
//                    String pm = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID);
//                    Toast.makeText(context, position + " " + item.getId(), Toast.LENGTH_SHORT).show();
                    Fragment fragment = new DetailWaitingHealthRecordFragment();
                    openNewFragment(view, fragment);

                    break;
                case R.id.cancelButton:
                    break;
            }
        }

        private void openNewFragment(View view, Fragment nextFragment) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
        }


    }
}
