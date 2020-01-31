package com.example.logregsiterapp.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.logregsiterapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LogAppFirebaseMessagingEventActivity extends AppCompatActivity {

    public static final String TAG = LogAppFirebaseMessagingEventActivity.class.getSimpleName();

    private Toolbar toolbar;
    private MaterialButton materialButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_app_firebase_messaging_event);

        toolbar = (Toolbar) findViewById(R.id.CustomToolBar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel("mohit",
                    "varma", NotificationManager.IMPORTANCE_LOW));
        }



        materialButton = (MaterialButton) findViewById(R.id.firebase_onclick_btn);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }
                                // Get new Instance ID token
                                token = task.getResult().getToken();
                                // Log and toast
                                Log.d(TAG, token);
                                Toast.makeText(LogAppFirebaseMessagingEventActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
