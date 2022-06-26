package com.example.usmile.doctor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorShowImagesAdapter extends RecyclerView.Adapter<DoctorShowImagesAdapter.ShowImagesViewHolder>{

//    private List<Tips> newTips;
    private List<String> imageList;
    private Context context;

    public DoctorShowImagesAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    public DoctorShowImagesAdapter() {
        this.imageList = new ArrayList<>();
    }

    public void setData(List<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ShowImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_picture_item, parent, false);

        context = parent.getContext();

        return new ShowImagesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ShowImagesViewHolder holder, int position) {
        String item = imageList.get(position);
        Bitmap bm = getRoundBitmap(decodeImage(item));
        holder.imageView.setImageBitmap(bm);
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.imageView.setBackgroundResource(0);
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

    @Override
    public int getItemCount() {
        if (imageList == null)
            return 0;
        return imageList.size();
    }

    public class ShowImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ShowImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
