package com.example.logregsiterapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.adapters.JsonAdapter;
import com.example.logregsiterapp.utils.GlobalDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JsonFileFragment extends Fragment {

    private String string;
    private InputStream inputStream;
    private List<String> titleList;

    private RecyclerView recyclerView;

    public JsonFileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_json_file, container, false);

        titleList = new LinkedList<>();


        recyclerView = (RecyclerView) view.findViewById(R.id.jsonfile_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        GlobalDialog.createDialogwithtitle(getActivity(), getJsonAsStringfromAsset());

        try {
            JSONArray jsonArray = new JSONArray(getJsonAsStringfromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString(getStringfromResource(R.string.json_object_key));
                titleList.add(title);
            }
            JsonAdapter jsonCustomRecyclerView = new JsonAdapter(getActivity(), titleList);
            recyclerView.setAdapter(jsonCustomRecyclerView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public String getJsonAsStringfromAsset() {
        try {
            inputStream = getActivity().getAssets().open(getStringfromResource(R.string.json_file_name));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            string = new String(buffer, getStringfromResource(R.string.json_charset));

        } catch (Exception e) {
            GlobalDialog.createDialogwithtitle(getActivity(), e.getMessage());
        }
        return string;
    }

    public String getStringfromResource(int id) {
        return getActivity().getResources().getString(id);
    }


}
