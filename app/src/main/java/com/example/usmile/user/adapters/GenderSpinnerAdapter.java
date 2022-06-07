package com.example.usmile.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usmile.R;
import com.example.usmile.account.Account;

import java.util.List;

public class GenderSpinnerAdapter extends ArrayAdapter<String> {

    public GenderSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gender_selected, parent, false);

        TextView genderTextView = convertView.findViewById(R.id.genderSelectedTextView);

        String gender = this.getItem(position);

        if (gender != null)
            genderTextView.setText(gender);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gender_list, parent, false);

        TextView genderTextView = convertView.findViewById(R.id.genderTextView);

        String gender = this.getItem(position);

        if (gender != null)
            genderTextView.setText(gender);

        return convertView;
    }
}
