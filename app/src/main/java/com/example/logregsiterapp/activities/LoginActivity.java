package com.example.logregsiterapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.database.DatabaseConnection;
import com.example.logregsiterapp.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    //private Button mLoginButton;
    private MaterialButton materialButton_loginBtn;
    private TextView mSignUpTextView;
    //private EditText userEmail, userpassword;
    private TextInputEditText mTextInputEditText_userEmail, mTextInputEditText_userPassword;

    private DatabaseConnection databaseConnection;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        final View view = findViewById(R.id.context_relative_for_login);


        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_USER, MODE_PRIVATE);

        editor = sharedPreferences.edit();
        mTextInputEditText_userEmail = (TextInputEditText) findViewById(R.id.edittxt);
        mTextInputEditText_userPassword = (TextInputEditText) findViewById(R.id.password_editext);
        materialButton_loginBtn = (MaterialButton) findViewById(R.id.login_button);
        databaseConnection = new DatabaseConnection(LoginActivity.this.getApplicationContext());

        //mLoginButton = (Button) findViewById(R.id.login_button);
        mSignUpTextView = (TextView) findViewById(R.id.sign_button);

        materialButton_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String emailID = mTextInputEditText_userEmail.getText().toString();
                String passwordtobecheck = mTextInputEditText_userPassword.getText().toString();
                if (check_fields_are_empty_or_not(mTextInputEditText_userEmail, mTextInputEditText_userPassword)) {
                    mTextInputEditText_userEmail.setError(getStringfromResource(R.string.enter_email_msg_error));
                    mTextInputEditText_userPassword.setError(getStringfromResource(R.string.enter_password_msg_error));
                } else {
                    if (emailValidation(emailID)) {
                        if (check_user_Already_in_Database(emailID)) {
                            String getpassword = getUserPasswordfromDBthroughEmail(emailID);
                            if (getpassword.equalsIgnoreCase(passwordtobecheck)) {
                                editor.putString(Constants.SHARED_PREFERENCE_USER_NAME, getUserName(emailID));
                                editor.putString(Constants.SHARED_PREFERENCE_USER_EMAIL, emailID);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Snackbar.make(view, R.string.wrong_password, Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(view, R.string.user_not_exists, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(view, R.string.enter_proper_email, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterationActivity.class);
                startActivity(intent);
            }
        });
        mTextInputEditText_userPassword.setLongClickable(false);
        mTextInputEditText_userPassword.setTextIsSelectable(false);

        Log.d(TAG, "onCreate: ");

    }

    public boolean check_user_Already_in_Database(String email) {
        return databaseConnection.CheckEmailAlreadyInDatabase(email);
    }

    public String getUserPasswordfromDBthroughEmail(String email) {
        return databaseConnection.getPasswordofUserthroughEmail(email);
    }

    public boolean emailValidation(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String getUserName(String email) {
        return databaseConnection.getUsername(email);
    }

    public boolean check_fields_are_empty_or_not(EditText usermail, EditText userpassword) {
        if (usermail.getText().toString().isEmpty() || userpassword.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
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
