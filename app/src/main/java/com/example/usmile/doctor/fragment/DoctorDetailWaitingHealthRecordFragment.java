package com.example.usmile.doctor.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.doctor.DoctorMainActivity;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.user.adapters.MultiHealthRecordAdapter;
import com.example.usmile.user.fragment.HealthRecordFragment;
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

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DoctorDetailWaitingHealthRecordFragment extends Fragment implements View.OnClickListener{
    DoctorMainActivity main;

    TextView senderGender;
    TextView sendRecordDate;
    TextView senderAge;
    TextView askForAdviceEditText;
    TextView senderName;

    Button acceptButton;
    Button cancelButton;

    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;


    PreferenceManager preferenceManager;

    AlertDialog cancelDialog;
    AlertDialog.Builder dialogBuilder;

    Fragment fragment;

    Doctor doctor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_detail_waiting_health_record, container, false);
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctor = (Doctor) bundle.getSerializable(AccountFactory.DOCTORSTRING);
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        preferenceManager = new PreferenceManager(getContext());

        main = (DoctorMainActivity) getActivity();

        senderGender = (TextView) view.findViewById(R.id.senderGender);
        sendRecordDate = (TextView) view.findViewById(R.id.sendRecordDate);
        senderAge = (TextView) view.findViewById(R.id.senderAge);
        askForAdviceEditText = (TextView) view.findViewById(R.id.askForAdviceEditText);
        senderName = (TextView) view.findViewById(R.id.senderName);

        acceptButton = (Button) view.findViewById(R.id.acceptButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        firstImageView = (ImageView) view.findViewById(R.id.firstPicture);
        secondImageView = (ImageView) view.findViewById(R.id.secondPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.thirdPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.fourthPicture);

        acceptButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        loadWaitingHealthRecordDetails();
        try{
            loadUserInfor();

        }
        catch (Exception e) {
            Log.d("ERR", e.getMessage());
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.acceptButton:
            {
                try{
                    createAcceptDialog();
                }
                catch (Exception e)
                {
                    Log.e("CANCEL DIALOG",e.getMessage());
                }
            }
//                fragment = new HealthRecordFragment();
//                openNewFragment(view, fragment);
                break;
            case R.id.cancelButton:
                try{
                    createCancelDialog();
                }
                catch (Exception e)
                {
                    Log.e("CANCEL DIALOG",e.getMessage());
                }
                break;
        }
    }

    private int calculateAge(String date) throws ParseException {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        SimpleDateFormat strFormat1 = new   SimpleDateFormat("dd/MM/yyyy");
        Date dateObj1 = strFormat1.parse(date);
        int birth = dateObj1.getYear() + 1900;
        int age = year - birth;
        Log.d("DOB", dateObj1.toString());
        return  age;
    }

    private void loadUserInfor()
    {
        String userId = preferenceManager.getString(Constants.KEY_GET_USER_ID);
        Log.d("USERID", userId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_ACCOUNT).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        senderGender.setText(doc.getString(Constants.KEY_ACCOUNT_GENDER));
                        senderName.setText(doc.getString(Constants.KEY_ACCOUNT_FULL_NAME));
                        String birthday = doc.getString(Constants.KEY_ACCOUNT_DOB);
                        try {
                            int age = calculateAge(birthday);

                            senderAge.setText(String.valueOf(age));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.d("DETA-W-HR", "No such document");
                    }
                } else {
                    Log.d("DETA-W-HR", "get failed with ", task.getException());
                }
            }
        });

    }
    private void loadWaitingHealthRecordDetails()
    {
        String healthRecordId = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID);
//        Log.d("HR ID", healthRecordId );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .whereEqualTo(Constants.KEY_HEALTH_RECORD_ID, healthRecordId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                        List<String> healthPictures = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_PICTURES);
                        String description = doc.getString(Constants.KEY_HEALTH_RECORD_DESCRIPTION);
                        String sendDate = doc.getString(Constants.KEY_HEALTH_RECORD_DATE);

                        encodeImage1 = healthPictures.get(0);
                        encodeImage2 = healthPictures.get(1);
                        encodeImage3 = healthPictures.get(2);
                        encodeImage4 = healthPictures.get(3);

                        firstImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage1)));
                        secondImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage2)));
                        thirdImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage3)));
                        fourthImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage4)));


                        firstImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        secondImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        thirdImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        fourthImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                        firstImageView.setBackgroundResource(0);
                        secondImageView.setBackgroundResource(0);
                        thirdImageView.setBackgroundResource(0);
                        fourthImageView.setBackgroundResource(0);

                        sendRecordDate.setText(sendDate);
                        askForAdviceEditText.setText(description);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void acceptHealthRecord() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));

//        String dentistId = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_DELETED);
//        del.add(preferenceManager.getString(Constants.KEY_ACCOUNT_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_HEALTH_RECORD_DENTIST_ID, doctor.getId());

        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    showToast("Updated successfully");

                })
                .addOnFailureListener(e -> {
                    Log.e("update health record", e.getMessage());
                });
    }

    public void createAcceptDialog(){
        dialogBuilder = new AlertDialog.Builder(main);
        final View quitPopup = getLayoutInflater().inflate(R.layout.popup_doctor_accept_health_record, null);

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
                acceptHealthRecord();
                cancelDialog.dismiss();
                try{
                    main.navigationView.getMenu().getItem(2).setChecked(true);
                    main.current_id = main.navigationView.getMenu().getItem(2).getItemId();
                }catch (Exception e)
                {
                    Log.e("135",e.getMessage());
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);

                fragment = new ReceivedHealthRecordListFragment();
                fragment.setArguments(bundle);
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

    private void cancelHealthRecord() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = db.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));

        List<String> del = preferenceManager.getListString(Constants.KEY_HEALTH_RECORD_DELETED);
        del.add(preferenceManager.getString(Constants.KEY_ACCOUNT_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_HEALTH_RECORD_DELETED, del);

        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    showToast("Updated successfully");

                })
                .addOnFailureListener(e -> {
                    Log.e("update health record", e.getMessage());
                });
    }

    public void createCancelDialog(){
        dialogBuilder = new AlertDialog.Builder(main);
        final View quitPopup = getLayoutInflater().inflate(R.layout.popup_doctor_cancel_health_record, null);

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
                cancelHealthRecord();
                cancelDialog.dismiss();

                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountFactory.DOCTORSTRING, doctor);

                Fragment fragment = new WaitingHealthRecordListFragment();
                fragment.setArguments(bundle);
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

    public Bitmap getRoundBitmap(Bitmap bitmap) {

        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());

        Canvas canvas = new Canvas(bitmapRounded);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0.0f, 0.0f, min, min)), min/8, min/8, paint);

        return bitmapRounded;
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