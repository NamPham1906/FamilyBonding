package com.example.usmile.doctor.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.doctor.DoctorMainActivity;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorGiveSpecificAdviceFragment extends Fragment implements View.OnClickListener{

    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;

    EditText firstDetailAdvice;
    EditText secondDetailAdvice;
    EditText thirdDetailAdvice;
    EditText fourthDetailAdvice;
    EditText doctorRequirementTextView;

    TextView sendButton;

    PreferenceManager preferenceManager;

    AlertDialog cancelDialog;
    AlertDialog.Builder dialogBuilder;

    Fragment fragment;

    DoctorMainActivity main;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_give_specific_advice, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(getContext());

        main = (DoctorMainActivity) getActivity();

        firstDetailAdvice = (EditText) view.findViewById(R.id.firstDetailAdvice);
        secondDetailAdvice = (EditText) view.findViewById(R.id.secondDetailAdvice);
        thirdDetailAdvice = (EditText) view.findViewById(R.id.thirdDetailAdvice);
        fourthDetailAdvice = (EditText) view.findViewById(R.id.fourthDetailAdvice);
        doctorRequirementTextView = (EditText) view.findViewById(R.id.doctorRequirementTextView);

        firstImageView = (ImageView) view.findViewById(R.id.patientFirstDetailPicture);
        secondImageView = (ImageView) view.findViewById(R.id.patientSecondDetailPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.patientThirdDetailPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.patientFourthDetailPicture);

        sendButton = (TextView) view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        loadHealthRecordDetails();

    }

    private void loadHealthRecordDetails() {
        String healthRecordId = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_HEALTH_RECORD_ID, healthRecordId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                        List<String> healthPictures = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_PICTURES);

                        encodeImage1 = healthPictures.get(0);
                        encodeImage2 = healthPictures.get(1);
                        encodeImage3 = healthPictures.get(2);
                        encodeImage4 = healthPictures.get(3);

                        firstImageView.setImageBitmap(decodeImage(encodeImage1));
                        secondImageView.setImageBitmap(decodeImage(encodeImage2));
                        thirdImageView.setImageBitmap(decodeImage(encodeImage3));
                        fourthImageView.setImageBitmap(decodeImage(encodeImage4));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sendButton:
                if (!isCompleted())
                    return;
                try{
                    createSendAdvicesDialog();
                }
                catch (Exception e)
                {
                    Log.e("CANCEL DIALOG",e.getMessage());
                }
                break;
        }
    }

    private void sendAdvices() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));

        List<String> advices = new ArrayList<String>();
        if(!isFillEditText(firstDetailAdvice))
            advices.add("");
        else
            advices.add(firstDetailAdvice.getText().toString().trim());

        if(!isFillEditText(secondDetailAdvice))
            advices.add("");
        else
            advices.add(secondDetailAdvice.getText().toString().trim());

        if(!isFillEditText(thirdDetailAdvice))
            advices.add("");
        else
            advices.add(thirdDetailAdvice.getText().toString().trim());

        if(!isFillEditText(fourthDetailAdvice))
            advices.add("");
        else
            advices.add(fourthDetailAdvice.getText().toString().trim());

        advices.add(doctorRequirementTextView.getText().toString().trim());

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_HEALTH_RECORD_ADVICES, advices);
        updates.put(Constants.KEY_HEALTH_RECORD_ACCEPTED, true);

        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    showToast("Updated successfully");

                })
                .addOnFailureListener(e -> {
                    Log.e("update health record", e.getMessage());
                });
    }

    public void createSendAdvicesDialog(){
        dialogBuilder = new AlertDialog.Builder(main);
        final View quitPopup = getLayoutInflater().inflate(R.layout.popup_send_advices_health_record, null);

        Button quitBtn = (Button) quitPopup.findViewById(R.id.btnQuit);
        Button cancelBtn = (Button) quitPopup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(quitPopup);
        cancelDialog = dialogBuilder.create();
        cancelDialog.show();
        cancelDialog.setCanceledOnTouchOutside(false);
        cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dimiss dialog
                sendAdvices();
                cancelDialog.dismiss();
                fragment = new ReceivedHealthRecordListFragment();
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

    private boolean warningFillEditText(EditText editText) {
        String input = editText.getText().toString().trim();

        if (input.isEmpty()) {
            editText.setError("Không được để trống");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }
    private boolean isFillEditText(EditText editText) {
        String input = editText.getText().toString().trim();

        if (input.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isCompleted() {
        if (!warningFillEditText(doctorRequirementTextView))
            return false;
        return true;
    }

    private void openNewFragment(View view, Fragment nextFragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}