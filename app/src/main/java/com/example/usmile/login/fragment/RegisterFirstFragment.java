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
import com.example.usmile.account.Account;
import com.example.usmile.account.AccountFactory;
import com.example.usmile.user.adapters.ActorSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class RegisterFirstFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinner;
    ActorSpinnerAdapter spinnerAdapter;

    Button continueButton;
    Account newAccount;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        continueButton = (Button) view.findViewById(R.id.continueButton);

        spinner = (Spinner) view.findViewById(R.id.actorSpinner);
        spinnerAdapter = new ActorSpinnerAdapter(getContext(), R.layout.item_actor_selected, getListAccount());
        spinnerAdapter.setDropDownViewResource(R.layout.item_actor_list);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);
        continueButton.setOnClickListener(this);
    }

    private List<Account> getListAccount() {
        String testEmail = "test@gmail.com";

        List<Account> list = new ArrayList<>();

        Account user = AccountFactory.createAccount(AccountFactory.USERSTRING, testEmail);
        Account doctor = AccountFactory.createAccount(AccountFactory.DOCTORSTRING, testEmail);
        Account admin = AccountFactory.createAccount(AccountFactory.ADMINSTRING, testEmail);

        list.add(user);
        list.add(doctor);
        list.add(admin);

        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_first, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), spinnerAdapter.getItem(i).typeVietsub(), Toast.LENGTH_SHORT).show();

        String type = spinnerAdapter.getItem(i).type();
        newAccount = AccountFactory.createAccount(type,"");


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.continueButton:

                Bundle bundle = new Bundle();
                bundle.putSerializable("NEW_ACCOUNT", newAccount);

                Fragment secondFragment = new RegisterSecondFragment();
                secondFragment.setArguments(bundle);

                openNewFragment(secondFragment);
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