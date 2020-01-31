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

public class VolleyAdapter extends RecyclerView.Adapter<VolleyAdapter.CustomViewHolder> {

    private Context context;
    private List<String> titleList;

    public VolleyAdapter(Context context, List<String> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.titlesingleitemview,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        String title = titleList.get(position);
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById((R.id.title_single_item));
        }
    }
}
