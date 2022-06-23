package com.example.usmile.user.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.login.fragment.RegisterFirstFragment;
import com.example.usmile.user.UserMainActivity;
import com.example.usmile.user.models.HealthRecord;
import com.example.usmile.utilities.Constants;
import com.example.usmile.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class CollectPictureFragment extends Fragment implements View.OnClickListener {

    FragmentManager fragmentManager;

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;
    Button sendBtn;
    EditText descriptionEditText;

    PreferenceManager preferenceManager;
    ProgressDialog pd;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstImageView = (ImageView) view.findViewById(R.id.firstImageView);
        secondImageView = (ImageView) view.findViewById(R.id.secondImageView);
        thirdImageView = (ImageView) view.findViewById(R.id.thirdImageView);
        fourthImageView = (ImageView) view.findViewById(R.id.fourthImageView);

        firstImageView.setOnClickListener(this);
        secondImageView.setOnClickListener(this);
        thirdImageView.setOnClickListener(this);
        fourthImageView.setOnClickListener(this);

        pd = new ProgressDialog(getContext());

        preferenceManager = new PreferenceManager(getContext());

        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        sendBtn = (Button) view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCompleted())
                    return;

                sendHealthRecord();
//                updateIdHealthRecord();
                Fragment fragment = new HealthRecordFragment();
                openNewFragment(fragment);
            }
        });
    }

    private void sendHealthRecord() {
        pd.setTitle("Gửi hồ sơ...");
        pd.show();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> newHealthRecord = new HashMap<>();

        String userID = preferenceManager.getString(Constants.KEY_ACCOUNT_ID);
        String description = descriptionEditText.getText().toString().trim();
        List<String> healthPictures = new ArrayList<>();
        healthPictures.add(encodeImage1);
        healthPictures.add(encodeImage2);
        healthPictures.add(encodeImage3);
        healthPictures.add(encodeImage4);

        List<String> advices = new ArrayList<>();
        List<String> delete = new ArrayList<>();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String sendDate = sdf.format(new Date());

        String id = UUID.randomUUID().toString();

        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_ID, id);
        newHealthRecord.put(Constants.KEY_ACCOUNT_ID, userID);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_DESCRIPTION, description);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_PICTURES, healthPictures);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_ADVICES, advices);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_DELETED, delete);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_ACCEPTED, false);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_DATE, sendDate);
        newHealthRecord.put(Constants.KEY_HEALTH_RECORD_DENTIST_ID, "");

        database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(id)
                .set(newHealthRecord)
                .addOnSuccessListener(documentReference -> {
//                    preferenceManager.putString(Constants.KEY_HEALTH_RECORD_ID, documentReference.getId());

                    pd.dismiss();
                    Toast.makeText(getContext(),"upload successed",Toast.LENGTH_LONG).show();

                })
                .addOnFailureListener(exception -> {
                    pd.dismiss();
                    Toast.makeText(getContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                });
    }

    private void updateIdHealthRecord() {
        // update -> health record id = document id
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        DocumentReference documentReference
                = database.collection(Constants.KEY_COLLECTION_HEALTH_RECORD)
                .document(preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));

        HashMap<String, Object> updateId = new HashMap<>();
        updateId.put(Constants.KEY_HEALTH_RECORD_ID,
                preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));

        documentReference.update(updateId)
                .addOnSuccessListener(unused -> {
                    Log.d("update record id", preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID));
                })
                .addOnFailureListener(e -> {
                    Log.e("error update record id", e.getMessage());
                });

    }

    private boolean isCompleted(){
        if (!isFillEditText(descriptionEditText))
            return false;
        if(encodeImage1.equals("") || encodeImage2.equals("") 
                || encodeImage3.equals("") || encodeImage4.equals(""))
        {
            String msg = "Chưa chụp đủ 4 bức ảnh";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isFillEditText(EditText editText) {
        String input = editText.getText().toString().trim();

        if (input.isEmpty()) {
            editText.setError("Không được để trống");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }


    public void openCamera(int requestCode) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }


    public void openGallery(int requestCode) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);
//        Intent galleryIntent = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent , requestCode );
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
                    firstImageView.setImageBitmap(getRoundBitmap(bp));
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case CAPTURE_SECOND_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(getRoundBitmap(bp));
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case CAPTURE_THIRD_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(getRoundBitmap(bp));
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case CAPTURE_FOURTH_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(getRoundBitmap(bp));
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
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            // Get the cursor
//            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            // Move to first row
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String imgDecodableString = cursor.getString(columnIndex);
//            cursor.close();

            switch (requestCode) {
                case LOAD_FIRST_IMAGE:
                    encodeImage1 = encodeImage(bp);
                    firstImageView.setImageBitmap(getRoundBitmap(bp));
//                    firstImageView.setImageBitmap(BitmapFactory
//                            .decodeFile(imgDecodableString));
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case LOAD_SECOND_IMAGE:
                    encodeImage2 = encodeImage(bp);

                    secondImageView.setImageBitmap(getRoundBitmap(bp));
//                    secondImageView.setImageBitmap(BitmapFactory
//                            .decodeFile(imgDecodableString));
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case LOAD_THIRD_IMAGE:
                    encodeImage3 = encodeImage(bp);

                    thirdImageView.setImageBitmap(getRoundBitmap(bp));
//                    thirdImageView.setImageBitmap(BitmapFactory
//                            .decodeFile(imgDecodableString));
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case LOAD_FOURTH_IMAGE:
                    encodeImage4 = encodeImage(bp);

                    fourthImageView.setImageBitmap(getRoundBitmap(bp));
//                    fourthImageView.setImageBitmap(BitmapFactory
//                            .decodeFile(imgDecodableString));
                    fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect_picture, container, false);
    }



    @Override
    public void onClick(View view) {

        int order = view.getId();
        openChooseSourceOfPictureDialog(order);

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

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

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
                    case R.id.firstImageView:
                        openCamera(CAPTURE_FIRST_IMAGE);
                        break;
                    case R.id.secondImageView:
                        openCamera(CAPTURE_SECOND_IMAGE);
                        break;
                    case R.id.thirdImageView:
                        openCamera(CAPTURE_THIRD_IMAGE);
                        break;
                    case R.id.fourthImageView:
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
                    case R.id.firstImageView:
                        openGallery(LOAD_FIRST_IMAGE);
                        break;
                    case R.id.secondImageView:
                        openGallery(LOAD_SECOND_IMAGE);
                        break;
                    case R.id.thirdImageView:
                        openGallery(LOAD_THIRD_IMAGE);
                        break;
                    case R.id.fourthImageView:
                        openGallery(LOAD_FOURTH_IMAGE);
                        break;
                }

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

}
