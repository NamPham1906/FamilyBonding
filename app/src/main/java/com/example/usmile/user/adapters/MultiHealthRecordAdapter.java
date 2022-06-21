package com.example.usmile.user.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.user.fragment.DetailAcceptedHealthRecordFragment;
import com.example.usmile.user.fragment.DetailWaitingHealthRecordFragment;
import com.example.usmile.user.fragment.HealthRecordFragment;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MultiHealthRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int WAITING_HEALTH_RECORD = 0;
    private static int ACCEPTED_HEALTH_RECORD = 1;

    private List<HealthRecord> healthRecords;
    private Context context;

    PreferenceManager preferenceManager;





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
            preferenceManager = new PreferenceManager(this.context);

            // should add this property to Health Record ?
            String dentistName = getDentistName(item);
            acceptedHealthRecordViewHolder.statusDetailTextView.setText("BS " + dentistName + " đã tiếp nhận");
        } else if (WAITING_HEALTH_RECORD == holder.getItemViewType()) {

            WaitingHealthRecordViewHolder waitingHealthRecordViewHolder = (WaitingHealthRecordViewHolder) holder;

            waitingHealthRecordViewHolder.sentDateTextView.setText(item.getSentDate());
        }

    }

    private String getDentistName(@NonNull HealthRecord hr)
    {
        preferenceManager = new PreferenceManager(this.context);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(hr.getDentistId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        preferenceManager.putString(Constants.KEY_GET_DENTIST_NAME,
                                doc.getString(Constants.KEY_ACCOUNT_FULL_NAME));
//                        Toast.makeText(context,doc.getId(),
//                                Toast.LENGTH_LONG).show();

                    } else {
                        Log.d("DEN-ID", "No such document");
                    }
                } else {
                    Log.d("DEN-ID", "get failed with ", task.getException());
                }
            }
        });
        String dentistName = preferenceManager.getString(Constants.KEY_GET_DENTIST_NAME);

        return dentistName;
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

        AlertDialog cancelDialog;
        AlertDialog.Builder dialogBuilder;



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
                    try{
                        preferenceManager.putString(Constants.KEY_HEALTH_RECORD_ID, item.getId());
                        Log.d("PM", preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));
                        Fragment fragment = new DetailWaitingHealthRecordFragment();
                        openNewFragment(view, fragment);
                    }
                    catch(Exception e)
                    {
                        Log.e("EDIT HR",e.getMessage());
                    }


                    break;
                case R.id.cancelButton:
                    preferenceManager.putString(Constants.KEY_HEALTH_RECORD_ID, item.getId());

                    try{
                        showAlertDialog(view.getContext());
                    }
                    catch (Exception e)
                    {
                        Log.e("CANCEL DIALOG",e.getMessage());
                    }
                    break;
            }
        }

        private void cancelHealthRecord() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                    .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("DELETE HR", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error deleting document", e.getMessage());
                        }
                    });
        }

        public void showAlertDialog(Context context) {
            dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View cancelPopup = inflater.inflate( R.layout.popup_cancel_health_record, null );

            Button quitBtn = (Button) cancelPopup.findViewById(R.id.btnQuit);
            Button cancelBtn = (Button) cancelPopup.findViewById(R.id.btnCancel);
            dialogBuilder.setView(cancelPopup);
            cancelDialog = dialogBuilder.create();
            cancelDialog.show();
            cancelDialog.setCanceledOnTouchOutside(false);
            cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            quitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dimiss dialog
                    cancelHealthRecord();
                    cancelDialog.dismiss();
                    //reload fragment
                    Fragment fragment = new HealthRecordFragment();
                    openNewFragment(view, fragment);

                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dimiss dialog
                    cancelDialog.dismiss();

                }
            });
        }

        private void openNewFragment(View view, Fragment nextFragment) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
        }
    }
}
