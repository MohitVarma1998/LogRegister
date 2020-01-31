package com.example.logregsiterapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.broadcast.ReceiveAlarmManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AlarmManagerActivity extends AppCompatActivity {

     private MaterialButton mSetAlarm;
     private TextInputEditText textInputEditTextSetTime;
     private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);

        toolbar = (Toolbar) findViewById(R.id.CustomToolBar);
        mSetAlarm = (MaterialButton) findViewById(R.id.set_alarm_button);
        textInputEditTextSetTime = (TextInputEditText) findViewById(R.id.set_time_edittext);


        mSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettime = textInputEditTextSetTime.getText().toString();
                if(gettime.matches("")){
                    Toast.makeText(AlarmManagerActivity.this, "You did not set time.", Toast.LENGTH_SHORT).show();
                }else {
                    int time = Integer.parseInt(textInputEditTextSetTime.getText().toString());
                    Intent intent = new Intent(AlarmManagerActivity.this, ReceiveAlarmManager.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC,System.currentTimeMillis() + time * 1000,pendingIntent);
                    Toast.makeText(AlarmManagerActivity.this, "Alarm is set", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
