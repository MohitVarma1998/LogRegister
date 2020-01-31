package com.example.logregsiterapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;

import java.util.List;

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.JsonViewHOlder> {

    private Context context;
    private List<String> list;


    public JsonAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public JsonAdapter.JsonViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.titlesingleitemview,parent,false);
        JsonViewHOlder jsonViewHOlder = new JsonViewHOlder(view);
        return  jsonViewHOlder;
    }

    @Override
    public void onBindViewHolder(@NonNull JsonViewHOlder holder, int position) {
        String title = list.get(position);
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class JsonViewHOlder extends RecyclerView.ViewHolder{
        TextView textView;
        public JsonViewHOlder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_single_item);
        }
    }
}