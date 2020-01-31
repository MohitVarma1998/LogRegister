package com.example.logregsiterapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CustomViewHolder> {

    //CLASS LEVEL CONSTANT
    public  final String NAME = "NAME : ";
    public  final String  EMAIL = "EMAIL : ";
    public  final String DATE_OF_BIRTH = "DATE OF BIRTH : ";


    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(this.NAME + user.getmName());
        holder.userEmail.setText(this.EMAIL + user.getmEmail());
        holder.userDOB.setText(this.DATE_OF_BIRTH + user.getmDOB());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, userEmail, userDOB;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name_db);
            userEmail = (TextView) itemView.findViewById(R.id.user_email_db);
            userDOB = (TextView) itemView.findViewById(R.id.user_dob_db);
        }
    }
}
