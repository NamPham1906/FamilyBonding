package com.example.usmile.user.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DetailAcceptedHealthRecordFragment extends Fragment {

    final int CAPTURE_FIRST_IMAGE = 1;
    final int CAPTURE_SECOND_IMAGE = 2;
    final int CAPTURE_THIRD_IMAGE = 3;
    final int CAPTURE_FOURTH_IMAGE = 4;

    final int LOAD_FIRST_IMAGE = 5;
    final int LOAD_SECOND_IMAGE = 6;
    final int LOAD_THIRD_IMAGE = 7;
    final int LOAD_FOURTH_IMAGE = 8;

    private TextView recordTimeTextView;
    private TextView doctorFullNameTextView;
    private TextView doctorWorkPlaceTextView;
    private TextView patientMessageTextView;

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;

    TextView firstDetailAdvice;
    TextView secondDetailAdvice;
    TextView thirdDetailAdvice;
    TextView fourthDetailAdvice;
    TextView summaryAdviceTextView;

    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    PreferenceManager preferenceManager;


    public DetailAcceptedHealthRecordFragment() {

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        recordTimeTextView = (TextView) view.findViewById(R.id.recordTimeTextView);
        doctorFullNameTextView = (TextView) view.findViewById(R.id.doctorFullNameTextView);
        doctorWorkPlaceTextView = (TextView) view.findViewById(R.id.doctorWorkPlaceTextView);
        patientMessageTextView = (TextView) view.findViewById(R.id.patientMessageTextView);

        firstDetailAdvice = (TextView) view.findViewById(R.id.firstDetailAdvice);
        secondDetailAdvice = (TextView) view.findViewById(R.id.secondDetailAdvice);
        thirdDetailAdvice = (TextView) view.findViewById(R.id.thirdDetailAdvice);
        fourthDetailAdvice = (TextView) view.findViewById(R.id.fourthDetailAdvice);
        summaryAdviceTextView = (TextView) view.findViewById(R.id.summaryAdviceTextView);

        firstImageView = (ImageView) view.findViewById(R.id.firstPicture);
        secondImageView = (ImageView) view.findViewById(R.id.secondPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.thirdPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.fourthPicture);

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
                        String description = doc.getString("description");
                        String sendDate = doc.getString("sendDate");

                        recordTimeTextView.setText("Hồ sơ ngày" + sendDate);

                        patientMessageTextView.setText(description);

                        encodeImage1 = healthPictures.get(0);
                        encodeImage2 = healthPictures.get(1);
                        encodeImage3 = healthPictures.get(2);
                        encodeImage4 = healthPictures.get(3);

                        firstImageView.setImageBitmap(decodeImage(encodeImage1));
                        secondImageView.setImageBitmap(decodeImage(encodeImage2));
                        thirdImageView.setImageBitmap(decodeImage(encodeImage3));
                        fourthImageView.setImageBitmap(decodeImage(encodeImage4));

                        List<String> advices = (ArrayList) doc.get(Constants.KEY_HEALTH_RECORD_ADVICES);

                        if(advices.get(0).equals(""))
                            firstDetailAdvice.setText("Không có tư vấn hình ảnh");
                        else
                            firstDetailAdvice.setText(advices.get(0));

                        if(advices.get(1).equals(""))
                            secondDetailAdvice.setText("Không có tư vấn hình ảnh này");
                        else
                            secondDetailAdvice.setText(advices.get(1));

                        if(advices.get(2).equals(""))
                            thirdDetailAdvice.setText("Không có tư vấn hình ảnh này");
                        else
                            thirdDetailAdvice.setText(advices.get(2));

                        if(advices.get(3).equals(""))
                            fourthDetailAdvice.setText("Không có tư vấn hình ảnh này");
                        else
                            fourthDetailAdvice.setText(advices.get(3));

                        summaryAdviceTextView.setText(advices.get(4));


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_accepted_health_record, container, false);
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

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void openCamera(int requestCode) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }


    public void openGallery(int requestCode) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // for capture
        if (requestCode < 5) {
            Bitmap bp;

            switch (requestCode) {
                case CAPTURE_FIRST_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage1 = encodeImage(bp);
                    firstImageView.setImageBitmap(bp);
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case CAPTURE_SECOND_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(bp);
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case CAPTURE_THIRD_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(bp);
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case CAPTURE_FOURTH_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(bp);
                    fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
            }
        }
        else {
            Uri selectedImage = data.getData();
            Bitmap bp = null;
            try {
                bp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                Log.i("GALERY", "Some exception " + e);
            }

            switch (requestCode) {
                case LOAD_FIRST_IMAGE:
                    encodeImage1 = encodeImage(bp);
                    firstImageView.setImageBitmap(bp);
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case LOAD_SECOND_IMAGE:
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(bp);
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case LOAD_THIRD_IMAGE:
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(bp);
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
                case LOAD_FOURTH_IMAGE:
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(bp);
                    fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    break;
            }
        }
    }

    private void openChooseSourceOfPictureDialog(int order) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_source_of_picture_layout);


        Window window = dialog.getWindow();

        if (window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;

        dialog.setCancelable(true);

        // binding here
        ImageView cameraImageView = dialog.findViewById(R.id.fromCamImageView);
        ImageView galleryImageView = dialog.findViewById(R.id.fromGalleryImageView);

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (order) {
                    case R.id.firstPicture:
                        openCamera(CAPTURE_FIRST_IMAGE);
                        break;
                    case R.id.secondPicture:
                        openCamera(CAPTURE_SECOND_IMAGE);
                        break;
                    case R.id.thirdPicture:
                        openCamera(CAPTURE_THIRD_IMAGE);
                        break;
                    case R.id.fourthPicture:
                        openCamera(CAPTURE_FOURTH_IMAGE);
                        break;
                }

                dialog.dismiss();
            }
        });

        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (order) {
                    case R.id.firstPicture:
                        openGallery(LOAD_FIRST_IMAGE);
                        break;
                    case R.id.secondPicture:
                        openGallery(LOAD_SECOND_IMAGE);
                        break;
                    case R.id.thirdPicture:
                        openGallery(LOAD_THIRD_IMAGE);
                        break;
                    case R.id.fourthPicture:
                        openGallery(LOAD_FOURTH_IMAGE);
                        break;
                }

                dialog.dismiss();

            }
        });

        dialog.show();

    }

}