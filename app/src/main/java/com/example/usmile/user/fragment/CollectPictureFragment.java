package com.example.usmile.user.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.usmile.R;


public class CollectPictureFragment extends Fragment implements View.OnClickListener {

    ImageView firstImageView;
    ImageView secondImageView;
    ImageView thirdImageView;
    ImageView fourthImageView;

    final int FIRST_IMAGE = 1;
    final int SECOND_IMAGE = 2;
    final int THIRD_IMAGE = 3;
    final int FOURTH_IMAGE = 4;

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
    }


    public void openCamera(int requestCode){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");

        switch (requestCode) {
            case FIRST_IMAGE:
                firstImageView.setImageBitmap(bp);
                firstImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case SECOND_IMAGE:
                secondImageView.setImageBitmap(bp);
                secondImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case THIRD_IMAGE:
                thirdImageView.setImageBitmap(bp);
                thirdImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case FOURTH_IMAGE:
                fourthImageView.setImageBitmap(bp);
                fourthImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
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
        int id = view.getId();

        switch (id) {
            case R.id.firstImageView:
                openCamera(FIRST_IMAGE);
                break;
            case R.id.secondImageView:
                openCamera(SECOND_IMAGE);
                break;
            case R.id.thirdImageView:
                openCamera(THIRD_IMAGE);
                break;
            case R.id.fourthImageView:
                openCamera(FOURTH_IMAGE);
                break;
        }
    }

}
