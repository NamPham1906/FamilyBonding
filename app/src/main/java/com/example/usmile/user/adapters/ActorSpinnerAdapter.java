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

public class ActorSpinnerAdapter extends ArrayAdapter<Account> {


    public ActorSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Account> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor_selected, parent, false);

        TextView actorTextView = convertView.findViewById(R.id.actorSelectedTextView);

        Account account = this.getItem(position);

        if (account != null)
            actorTextView.setText(account.typeVietsub());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor_list, parent, false);

        TextView actorTextView = convertView.findViewById(R.id.actorTextView);

        Account account = this.getItem(position);

        if (account != null)
            actorTextView.setText(account.typeVietsub());



        return convertView;
    }
}
