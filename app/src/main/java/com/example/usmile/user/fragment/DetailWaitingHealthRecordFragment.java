package com.example.usmile.user.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.UserMainActivity;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class DetailWaitingHealthRecordFragment extends Fragment implements View.OnClickListener {

    final int CAPTURE_FIRST_IMAGE = 1;
    final int CAPTURE_SECOND_IMAGE = 2;
    final int CAPTURE_THIRD_IMAGE = 3;
    final int CAPTURE_FOURTH_IMAGE = 4;

    final int LOAD_FIRST_IMAGE = 5;
    final int LOAD_SECOND_IMAGE = 6;
    final int LOAD_THIRD_IMAGE = 7;
    final int LOAD_FOURTH_IMAGE = 8;

    String encodeImage1 = "";
    String encodeImage2 = "";
    String encodeImage3 = "";
    String encodeImage4 = "";

    TextView timeDetailTextView;
    TextView askForAdviceEditText;

    Button editButton;
    Button cancelButton;

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;


    PreferenceManager preferenceManager;

    public DetailWaitingHealthRecordFragment(HealthRecord healthRecord) {

    }

    public DetailWaitingHealthRecordFragment() {

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        timeDetailTextView = (TextView) view.findViewById(R.id.timeDetailTextView);
        askForAdviceEditText = (TextView) view.findViewById(R.id.askForAdviceEditText);
        // text is changed -> enable edit button
        askForAdviceEditText.addTextChangedListener(textWatcher);

        editButton = (Button) view.findViewById(R.id.editButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        firstImageView = (ImageView) view.findViewById(R.id.firstPicture);
        secondImageView = (ImageView) view.findViewById(R.id.secondPicture);
        thirdImageView = (ImageView) view.findViewById(R.id.thirdPicture);
        fourthImageView = (ImageView) view.findViewById(R.id.fourthPicture);

        editButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        firstImageView.setOnClickListener(this);
        secondImageView.setOnClickListener(this);
        thirdImageView.setOnClickListener(this);
        fourthImageView.setOnClickListener(this);

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

                        List<String> healthPictures = (ArrayList) doc.get("healthPictures");
                        String description = doc.getString("description");
                        String sendDate = doc.getString("sendDate");

                        encodeImage1 = healthPictures.get(0);
                        encodeImage2 = healthPictures.get(1);
                        encodeImage3 = healthPictures.get(2);
                        encodeImage4 = healthPictures.get(3);

                        firstImageView.setImageBitmap(decodeImage(encodeImage1));
                        secondImageView.setImageBitmap(decodeImage(encodeImage2));
                        thirdImageView.setImageBitmap(decodeImage(encodeImage3));
                        fourthImageView.setImageBitmap(decodeImage(encodeImage4));

                        timeDetailTextView.setText("Hồ sơ ngày " + sendDate);
                        askForAdviceEditText.setText(description);
                        editButton.setEnabled(false);
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
        return inflater.inflate(R.layout.fragment_detail_waiting_health_record, container, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.firstPicture || id == R.id.secondPicture
                || id == R.id.thirdPicture || id == R.id.secondPicture)
        {
            openChooseSourceOfPictureDialog(id);
        }
        switch (id) {
            case R.id.editButton:
                updateHealthRecord();
                Fragment fragment = new HealthRecordFragment();
                openNewFragment(view, fragment);
                break;
            case R.id.cancelButton:
                cancelHealthRecord();
                break;
        }
    }

    private void updateHealthRecord() {
        String newDescription = askForAdviceEditText.getText().toString().trim();
        List<String> newHealthPictures = new ArrayList<>();
        newHealthPictures.add(encodeImage1);
        newHealthPictures.add(encodeImage2);
        newHealthPictures.add(encodeImage3);
        newHealthPictures.add(encodeImage4);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String sendDate = sdf.format(new Date());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference
                = database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));


        HashMap<String, Object> updates = new HashMap<>();

        updates.put(Constants.KEY_HEALTH_RECORD_PICTURES, newHealthPictures);
        updates.put(Constants.KEY_HEALTH_RECORD_DESCRIPTION, newDescription);
        updates.put(Constants.KEY_HEALTH_RECORD_DATE, sendDate);
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    showToast("Updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("update health record", e.getMessage());
                });

    }
    private void cancelHealthRecord() {
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
                    enableEditButton();

                    break;
                case CAPTURE_SECOND_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(bp);
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case CAPTURE_THIRD_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(bp);
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case CAPTURE_FOURTH_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(bp);
                    fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

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
                    enableEditButton();

                    break;
                case LOAD_SECOND_IMAGE:
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(bp);
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case LOAD_THIRD_IMAGE:
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(bp);
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case LOAD_FOURTH_IMAGE:
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(bp);
                    fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

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

    private void enableEditButton () {
        editButton.setEnabled(true);
        Drawable img = getContext().getResources().getDrawable( R.drawable.small_green_button_shape );
        editButton.setBackground(img);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
//            enableEditButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}