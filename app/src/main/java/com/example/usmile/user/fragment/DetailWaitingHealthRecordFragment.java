package com.example.usmile.user.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.User;
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

    UserMainActivity main;

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
    EditText askForAdviceEditText;

    Button editButton;
    Button cancelButton;

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;


    PreferenceManager preferenceManager;

    AlertDialog cancelDialog;
    AlertDialog.Builder dialogBuilder;

    Fragment fragment;
    User user;

    public DetailWaitingHealthRecordFragment(HealthRecord healthRecord) {

    }

    public DetailWaitingHealthRecordFragment() {

    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);

        }
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        main = (UserMainActivity) getActivity();
        preferenceManager = new PreferenceManager(getContext());

        timeDetailTextView = (TextView) view.findViewById(R.id.timeDetailTextView);
        askForAdviceEditText = (EditText) view.findViewById(R.id.askForAdviceEditText);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_detail_waiting_health_record, container, false);
    }

    private void loadHealthRecordDetails() {

        String healthRecordId = preferenceManager.getString(Constants.KEY_HEALTH_RECORD_ID);
        Log.d("HR ID", healthRecordId );
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

                        firstImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage1)));
                        secondImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage2)));
                        thirdImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage3)));
                        fourthImageView.setImageBitmap(getRoundBitmap(decodeImage(encodeImage4)));


                        firstImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        secondImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        thirdImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        fourthImageView.setScaleType(ImageView.ScaleType.FIT_XY);

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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.firstPicture || id == R.id.secondPicture
                || id == R.id.thirdPicture || id == R.id.secondPicture)
        {
            openChooseSourceOfPictureDialog(id);
        }
        switch (id) {
            case R.id.editButton:
                if(!isCompleted())
                    return;
                updateHealthRecord();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountFactory.USERSTRING, user);

                Fragment fragment = new HealthRecordFragment();
                fragment.setArguments(bundle);
                openNewFragment(view,fragment);
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

    public void createCancelDialog(){
        dialogBuilder = new AlertDialog.Builder(main);
        final View quitPopup = getLayoutInflater().inflate(R.layout.popup_cancel_health_record, null);

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
                bundle.putSerializable(AccountFactory.USERSTRING, user);

                Fragment fragment = new HealthRecordFragment();
                fragment.setArguments(bundle);
                openNewFragment(view,fragment);

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

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
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
                    firstImageView.setImageBitmap(getRoundBitmap(bp));
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case CAPTURE_SECOND_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(getRoundBitmap(bp));
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case CAPTURE_THIRD_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(getRoundBitmap(bp));
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case CAPTURE_FOURTH_IMAGE:
                    bp = (Bitmap) data.getExtras().get("data");
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(getRoundBitmap(bp));
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
                    firstImageView.setImageBitmap(getRoundBitmap(bp));
                    firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case LOAD_SECOND_IMAGE:
                    encodeImage2 = encodeImage(bp);
                    secondImageView.setImageBitmap(getRoundBitmap(bp));
                    secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case LOAD_THIRD_IMAGE:
                    encodeImage3 = encodeImage(bp);
                    thirdImageView.setImageBitmap(getRoundBitmap(bp));
                    thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    enableEditButton();

                    break;
                case LOAD_FOURTH_IMAGE:
                    encodeImage4 = encodeImage(bp);
                    fourthImageView.setImageBitmap(getRoundBitmap(bp));
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
        boolean nullString = false;
        public void afterTextChanged(Editable s) {
            if(!nullString)
                enableEditButton();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("560", s.toString());
            if (s.toString().equals(""))
            {
                nullString = true;
            }
            else {
                nullString = false;
            }
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

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

    private boolean isCompleted(){
        if (!isFillEditText(askForAdviceEditText))
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

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}