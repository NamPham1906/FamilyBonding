package com.example.usmile.user.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;

import java.util.ArrayList;
import java.util.List;

public class ShowImagesAdapter extends RecyclerView.Adapter<ShowImagesAdapter.ShowImagesViewHolder>{

//    private List<Tips> newTips;
    private List<String> imagesList;
    private String sentDate;
    private String sentMessage;
    private Context context;

    public ShowImagesAdapter(List<String> imageList, String sentDate, String sentMessage) {
        this.sentDate = sentDate;
        this.sentMessage = sentMessage;
        this.imagesList = imageList;
    }
    public String getDate(){return this.sentDate;}
    public int getImg(){return imagesList.size();}

    public ShowImagesAdapter() {
        this.imagesList = new ArrayList<>();
    }

    public void setData(List<String> imageList) {
        this.imagesList = imageList;
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
        String item = imagesList.get(position);
        Bitmap bm = getRoundBitmap(decodeImage(item));
        holder.imageView.setImageBitmap(bm);
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.imageView.setBackgroundResource(0);
        Log.d("1006 adap", String.valueOf(imagesList.size()) + " - " + sentMessage + " - " + sentDate);
        try{
            holder.patientSentMessageTextView.setText(sentMessage);
            holder.showImagesDate.setText(sentDate);
        }
        catch (Exception e)
        {
            Log.d("81", e.getMessage());
        }


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
        if (imagesList == null)
            return 0;
        return imagesList.size();
    }

    public class ShowImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView patientSentMessageTextView;
        TextView showImagesDate;


        public ShowImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            showImagesDate = (TextView) itemView.findViewById(R.id.showImagesDate);
            patientSentMessageTextView = (TextView) itemView.findViewById(R.id.patientSentMessageTextView);
        }
    }
}
