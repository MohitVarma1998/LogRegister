package com.example.logregsiterapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.database.DatabaseConnection;
import com.example.logregsiterapp.model.User;
import com.example.logregsiterapp.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterationActivity extends AppCompatActivity {

    public static final String TAG = RegisterationActivity.class.getSimpleName();

    private TextInputEditText mName, mEmail, mPassword, mdob;
    private MaterialButton register, login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        final View view = findViewById(R.id.context_relative);

        calendar = Calendar.getInstance();

        mName = (TextInputEditText) findViewById(R.id.registerF_username);
        mEmail = (TextInputEditText) findViewById(R.id.registerF_useremail);
        mPassword = (TextInputEditText) findViewById(R.id.registerF_userpassword);
        mdob = (TextInputEditText) findViewById(R.id.registerF_userdob);

        register = (MaterialButton) findViewById(R.id.registration_button);

        login = (MaterialButton) findViewById(R.id.taketo_loginscreen);

        final DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                formateDate();
            }
        };

        mdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterationActivity.this, datepicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        final DatabaseConnection connection = new DatabaseConnection(RegisterationActivity.this.getApplicationContext());

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_USER, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (mName.getText().toString().isEmpty() || mEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty() || mdob.getText().toString().isEmpty()) {
                    Snackbar.make(view, R.string.fields_cannot_empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (emailValidation(mEmail.getText().toString())) {
                        if (!checkIfemailalreadyRegistered(mEmail.getText().toString())) {
                            User user = new User(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString(), mdob.getText().toString());
                            boolean test = connection.mInsertUserIntoDatabase(user);
                            if (test) {
                                Snackbar.make(view, R.string.user_added_successfully, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(view, R.string.user_not_added, Snackbar.LENGTH_SHORT).show();
                            }
                            usertoLoggedIn();
                            clearEditext(mName, mEmail, mPassword, mdob);
                            Intent intent = new Intent(RegisterationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(view, R.string.already_registered, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(view, R.string.enter_proper_email, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Log.d(TAG, "onCreate: ");

    }

    public void clearEditext(EditText name, EditText email, EditText mPassword, EditText mdob) {
        name.setText(Constants.CLEAR_EDITTEXT);
        email.setText(Constants.CLEAR_EDITTEXT);
        mPassword.setText(Constants.CLEAR_EDITTEXT);
        mdob.setText(Constants.CLEAR_EDITTEXT);
    }

    public void formateDate() {
        String date = getStringfromResource(R.string.date_format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date, Locale.US);
        mdob.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public boolean emailValidation(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void usertoLoggedIn() {
        editor.putString(Constants.SHARED_PREFERENCE_USER_NAME, mName.getText().toString());
        editor.putString(Constants.SHARED_PREFERENCE_USER_EMAIL, mEmail.getText().toString());
        editor.apply();
    }

    public boolean checkIfemailalreadyRegistered(String email) {
        return new DatabaseConnection(this).CheckEmailAlreadyInDatabase(email);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    public String getStringfromResource(int id) {
        return this.getResources().getString(id);
    }
}

//async task
//volley
//retrofit
//okhttp