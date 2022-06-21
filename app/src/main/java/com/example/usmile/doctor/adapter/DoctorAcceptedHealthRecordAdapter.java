package com.example.usmile.doctor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
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
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Predicate;


public class DoctorAcceptedHealthRecordAdapter extends RecyclerView.Adapter<DoctorAcceptedHealthRecordAdapter.DoctorAcceptedHealthRecordViewHolder>{

    private List<HealthRecord> healthRecords;
    private Context context;
    PreferenceManager preferenceManager;

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
        preferenceManager = new PreferenceManager(this.context);
        HealthRecord item = healthRecords.get(position);

        if (item == null)
            return;

        holder.patientMessage.setText(item.getDescription());
        holder.patientSendDate.setText(item.getSentDate());

        userInfor(item);
        try
        {
            String ava = preferenceManager.getString(Constants.KEY_GET_USER_AVATAR);
            Bitmap bitmap = decodeImage(ava);
            holder.patientAvatar.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Log.e("ERR", e.getMessage());
        }

        holder.patientName.setText(preferenceManager.getString(Constants.KEY_GET_USER_NAME));

        // from account id -> load user avatar and name from firestore
//        holder.patientAvatar.setImageResource(R.drawable.example_avatar);
//        holder.patientName.setText("Nam 7749");

        if (item.isAccepted() == true && !item.getDentistId().equals("")) {
            holder.statusTextView.setText("Đã tư vấn");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_green));
        } else if (!item.isAccepted() && !item.getDentistId().equals("")) {
            holder.statusTextView.setText("Chờ tư vấn");
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_yellow));
        }

    }
    private void userInfor(HealthRecord hr) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(hr.getAccountId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        preferenceManager.putString(Constants.KEY_GET_USER_NAME,
                                doc.getString(Constants.KEY_ACCOUNT_FULL_NAME));
                        preferenceManager.putString(Constants.KEY_GET_USER_AVATAR,
                                doc.getString(Constants.KEY_ACCOUNT_AVATAR));
//                        Log.d("AVA", preferenceManager.getString(Constants.KEY_GET_USER_AVATAR));
//                        Log.d("NAME", preferenceManager.getString(Constants.KEY_GET_USER_NAME));


                    } else {
                        Log.d("DEN-ID", "No such document");
                    }
                } else {
                    Log.d("DEN-ID", "get failed with ", task.getException());
                }
            }
        });
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
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
