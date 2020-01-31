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

public class AsyncAdapter extends RecyclerView.Adapter<AsyncAdapter.AsyncHolder> {

    private Context context;
    private List<String> list;

    public AsyncAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AsyncHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.titlesingleitemview,parent,false);
        AsyncHolder asyncHolder = new AsyncHolder(view);
        return  asyncHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AsyncHolder holder, int position) {
        String title = list.get(position);
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AsyncHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public AsyncHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_single_item);
        }
    }
}
