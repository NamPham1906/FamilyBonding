package com.example.usmile.login.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.usmile.R;
import com.example.usmile.user.adapters.ActorSpinnerAdapter;
import com.example.usmile.user.adapters.GenderSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegisterSecondFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinner;
    GenderSpinnerAdapter spinnerAdapter;

    Button contButton;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contButton = (Button) view.findViewById(R.id.contButton);

        spinner = (Spinner) view.findViewById(R.id.genderSpinner);
        spinnerAdapter = new GenderSpinnerAdapter(getContext(), R.layout.item_gender_selected, getGenderList());
        spinnerAdapter.setDropDownViewResource(R.layout.item_gender_list);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);
        contButton.setOnClickListener(this);
    }

    private List<String> getGenderList() {
        List<String> list = new ArrayList<>();
        list.add("Nam");
        list.add("Nữ");
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_second, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), spinnerAdapter.getItem(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.contButton:
                Fragment fragment = new RegisterFragment();
                openNewFragment(fragment);
                break;
        }
    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}