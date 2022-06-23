package com.example.usmile.doctor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Doctor;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DoctorDetailReceivedHealthRecordFragment extends Fragment implements View.OnClickListener{


    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;

    ImageView patientImage;

    TextView firstDetailAdvice;
    TextView secondDetailAdvice;
    TextView thirdDetailAdvice;
    TextView fourthDetailAdvice;

    TextView doctorAdviceDetail;
    TextView patientSentMessageTextView;

    TextView healhRecordSendDate;
    TextView patientName;
    TextView patientGender;
    TextView patientAge;
    Doctor doctor;

    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_detail_received_health_record, container, false);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctor = (Doctor) bundle.getSerializable(AccountFactory.DOCTORSTRING);
        } else {
            showToast("null too");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        preferenceManager = new PreferenceManager(getContext());

        firstDetailAdvice = (TextView) view.findViewById(R.id.firstDetailAdvice);
        secondDetailAdvice = (TextView) view.findViewById(R.id.secondDetailAdvice);
        thirdDetailAdvice = (TextView) view.findViewById(R.id.thirdDetailAdvice);
        fourthDetailAdvice = (TextView) view.findViewById(R.id.fourthDetailAdvice);

        doctorAdviceDetail = (TextView) view.findViewById(R.id.doctorAdviceDetail);
        patientSentMessageTextView = (TextView) view.findViewById(R.id.patientSentMessageTextView);

        healhRecordSendDate = (TextView) view.findViewById(R.id.healhRecordSendDate);
        patientAge = (TextView) view.findViewById(R.id.patientAge);
        patientGender = (TextView) view.findViewById(R.id.patientGender);
        patientName = (TextView) view.findViewById(R.id.patientName);
        patientImage = (ImageView) view.findViewById(R.id.patientImage);

        firstImageView = (ImageView) view.findViewById(R.id.firstDetailPicture);
        secondImageView = (ImageView) view.findViewById(R.id.secondDetailPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.thirdDetailPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.fourthDetailPicture);

        firstImageView.setOnClickListener(this);
        secondImageView.setOnClickListener(this);
        thirdImageView.setOnClickListener(this);
        fourthImageView.setOnClickListener(this);

        loadReceivedHealthRecordDetails();
        try{
            loadUserInfor();
        }
        catch (Exception e) {
            Log.d("ERR", e.getMessage());
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
                        patientGender.setText(doc.getString(Constants.KEY_ACCOUNT_GENDER));
                        patientName.setText(doc.getString(Constants.KEY_ACCOUNT_FULL_NAME));
                        Bitmap bitmap = decodeImage(doc.getString(Constants.KEY_ACCOUNT_AVATAR));
                        bitmap = getRoundBitmap(bitmap);
                        patientImage.setImageBitmap(bitmap);
                        String birthday = doc.getString(Constants.KEY_ACCOUNT_DOB);
                        try {
                            int age = calculateAge(birthday);

                            patientAge.setText(String.valueOf(age));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.d("DETA-R-HR", "No such document");
                    }
                } else {
                    Log.d("DETA-R-HR", "get failed with ", task.getException());
                }
            }
        });

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

    private void loadReceivedHealthRecordDetails() {
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
                        List<String> advices = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_ADVICES);

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



                        healhRecordSendDate.setText(sendDate);
                        patientSentMessageTextView.setText(description);

                        if(advices.get(0).equals(""))
                            firstDetailAdvice.setText("Không có tư vấn cho hình ảnh này");
                        else
                            firstDetailAdvice.setText(advices.get(0));
                        if(advices.get(1).equals(""))
                            secondDetailAdvice.setText("Không có tư vấn cho hình ảnh này");
                        else
                            secondDetailAdvice.setText(advices.get(1));
                        if(advices.get(2).equals(""))
                            thirdDetailAdvice.setText("Không có tư vấn cho hình ảnh này");
                        else
                            thirdDetailAdvice.setText(advices.get(2));
                        if(advices.get(3).equals(""))
                            fourthDetailAdvice.setText("Không có tư vấn cho hình ảnh này");
                        else
                            fourthDetailAdvice.setText(advices.get(3));
                        doctorAdviceDetail.setText(advices.get(4));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void openNewFragment(View view, Fragment nextFragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentHolder, nextFragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstDetailPicture:
                showToast("First");
                break;
            case R.id.secondDetailPicture:
                showToast("Second");
                break;
            case R.id.thirdDetailPicture:
                showToast("Third");
                break;
            case R.id.fourthDetailPicture:
                showToast("Fourth");
                break;
        }


    }


}