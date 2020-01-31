package com.example.logregsiterapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.model.Title;

import java.util.List;

public class RetrofitAdapter extends RecyclerView.Adapter<RetrofitAdapter.RetrofitHolder> {

    private List<Title> titleList;
    private Context context;

    public RetrofitAdapter(List<Title> titleList, Context context) {
        this.titleList = titleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RetrofitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrofitsingleitem, parent, false);
        RetrofitHolder retrofitHolder = new RetrofitHolder(view);
        return retrofitHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RetrofitHolder holder, int position) {
        Title title = titleList.get(position);
        holder.userid.setText(title.getUserId() + "");
        holder.id.setText(title.getId() + "");
        holder.title.setText(title.getTitle());
        holder.body.setText(title.getBody());
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class RetrofitHolder extends RecyclerView.ViewHolder {
        TextView userid, id, title, body;

        public RetrofitHolder(@NonNull View itemView) {
            super(itemView);
            userid = (TextView) itemView.findViewById(R.id.userid);
            id = (TextView) itemView.findViewById(R.id.id);
            title = (TextView) itemView.findViewById(R.id.json_title);
            body = (TextView) itemView.findViewById(R.id.body);
        }
    }

}
