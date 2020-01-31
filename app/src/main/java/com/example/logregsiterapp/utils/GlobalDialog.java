package com.example.logregsiterapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.logregsiterapp.R;

public class GlobalDialog {
    public static void createDialogwithtitle(Context context,String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getResources().getString(R.string.exception_title));
        builder.setMessage(msg);
        builder.setPositiveButton(context.getResources().getString(R.string.exception_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.create().show();
    }
}
