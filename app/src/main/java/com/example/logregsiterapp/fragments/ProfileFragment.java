package com.example.logregsiterapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.utils.Constants;


public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView textView = (TextView) view.findViewById(R.id.name_ID);
        TextView textView1 = (TextView) view.findViewById(R.id.email_ID);

        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_USER, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        String name = sharedPreferences.getString(Constants.SHARED_PREFERENCE_USER_NAME,Constants.SHARED_PREFERENCE__NOVALUE);
        String email = sharedPreferences.getString(Constants.SHARED_PREFERENCE_USER_EMAIL,Constants.SHARED_PREFERENCE__NOVALUE);
        textView.setText(name);
        textView1.setText(email);
        return view;
    }

    public String getStringfromResource(int id){
        return getActivity().getResources().getString(id);
    }


}
