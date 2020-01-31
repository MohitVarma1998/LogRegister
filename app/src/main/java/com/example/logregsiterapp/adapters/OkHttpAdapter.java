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

public class OkHttpAdapter extends RecyclerView.Adapter<OkHttpAdapter.OkHttpHolder>{

    private Context context;
    private List<String> titleList;

    public OkHttpAdapter(Context context, List<String> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public OkHttpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.titlesingleitemview,parent,false);
        OkHttpHolder okHttpHolder = new OkHttpHolder(view);
        return okHttpHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OkHttpHolder holder, int position) {
        String title = titleList.get(position);
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class OkHttpHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public OkHttpHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_single_item);
        }
    }
}
