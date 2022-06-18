package com.example.usmile.user.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.models.Tips;

import java.util.ArrayList;
import java.util.List;

public class NewTipsAdapter extends RecyclerView.Adapter<NewTipsAdapter.NewTipsViewHolder>{

    private List<Tips> newTips;
    private Context context;
    String source;
    private Dialog WebDialog;

    public NewTipsAdapter(List<Tips> newTips) {
        this.newTips = newTips;
    }

    public NewTipsAdapter() {
        this.newTips = new ArrayList<>();
    }

    public void setData(List<Tips> newTips) {
        this.newTips = newTips;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public NewTipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_tip_item_layout, parent, false);

        context = parent.getContext();

        return new NewTipsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NewTipsViewHolder holder, int position) {
        Tips item = newTips.get(position);

        holder.sourceTextView.setText(item.getSourceWebsWebsite());
        holder.titleTextView.setText(item.getTitle());
        holder.timeTextView.setText("2 phút trước");
        holder.tipImageView.setImageResource(item.getResource());

        source = item.getUrl();
    }

    @Override
    public int getItemCount() {
        if (newTips == null)
            return 0;
        return newTips.size();
    }

    public class NewTipsViewHolder extends RecyclerView.ViewHolder {

        ImageView tipImageView;
        TextView titleTextView;
        TextView sourceTextView;
        TextView timeTextView;
        TextView checkDetailsButton;


        public NewTipsViewHolder(@NonNull View itemView) {
            super(itemView);

            tipImageView = (ImageView) itemView.findViewById(R.id.tipImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            sourceTextView = (TextView) itemView.findViewById(R.id.sourceTextView);

            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            checkDetailsButton = (TextView) itemView.findViewById(R.id.checkDetailButton);

            checkDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = source;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }
}
