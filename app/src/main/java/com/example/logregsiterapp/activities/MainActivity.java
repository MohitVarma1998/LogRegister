package com.example.logregsiterapp.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.background.InnerAsyncJsonFetcher;
import com.example.logregsiterapp.background.OkhttpAsyncTask;
import com.example.logregsiterapp.broadcast.ABroadcastReceiver;
import com.example.logregsiterapp.broadcast.NetworkBroadcastReceiver;
import com.example.logregsiterapp.broadcast.PowerBroadcastReceiver;
import com.example.logregsiterapp.database.DatabaseConnection;
import com.example.logregsiterapp.fragments.AsyncJsonFragment;
import com.example.logregsiterapp.fragments.JsonFileFragment;
import com.example.logregsiterapp.fragments.OkHttpFragment;
import com.example.logregsiterapp.fragments.ProfileFragment;
import com.example.logregsiterapp.fragments.RetrofitFragment;
import com.example.logregsiterapp.fragments.UsersFragment;
import com.example.logregsiterapp.fragments.VolleyFragment;
import com.example.logregsiterapp.utils.Actions;
import com.example.logregsiterapp.utils.Constants;
import com.example.logregsiterapp.utils.GlobalDialog;
import com.example.logregsiterapp.utils.WebService;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkBroadcastReceiver.SnackbarCallback {


    public static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView nameid, emailid;
    private TextView header_name_txt, header_email_txt;
    private Toolbar toolbar;
    private DatabaseConnection databaseConnection;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    BroadcastReceiver networkre = null;
    BroadcastReceiver powerbroadcastReceiver = null;
    IntentFilter intentFilter;
    IntentFilter powerIntentFilter;
    WifiManager wifiManager;

    WifiReceiver wifiReceiver;
    IntentFilter wifiintent;

    ABroadcastReceiver aBroadcastReceiver;
    IntentFilter aIntentfilter;
    View view;

    Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        view = findViewById(R.id.mainactivity);

        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.CustomToolBar);

        File me = new File( Environment.getExternalStorageDirectory().getPath()+"/Repnet");
        me.mkdirs();
        me.mkdir();

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.mainactivity);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        header_name_txt = (TextView) headerView.findViewById(R.id.Header_Name);
        header_email_txt = (TextView) headerView.findViewById(R.id.Header_Email);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        databaseConnection = new DatabaseConnection(MainActivity.this.getApplicationContext());

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_USER, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String name = sharedPreferences.getString(Constants.SHARED_PREFERENCE_USER_NAME, Constants.SHARED_PREFERENCE__NOVALUE);
        String email = sharedPreferences.getString(Constants.SHARED_PREFERENCE_USER_EMAIL, Constants.SHARED_PREFERENCE__NOVALUE);

        header_name_txt.setText(name);
        header_email_txt.setText(email);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawser_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //drawerLayout.openDrawer(Gravity.LEFT);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();

        toolbar.setTitle(R.string.usersList);
        toolbar.setTitleTextColor(Color.parseColor(getStringfromResource(R.string.toolbar_text_color)));
        setFragment(new UsersFragment());


        if (checkConnection(this)) {
            new InnerAsyncJsonFetcher().execute(WebService.URL);
            new OkhttpAsyncTask().execute(WebService.URL);
            GlobalDialog.createDialogwithtitle(this, getStringfromResource(R.string.internet_connected_msg));
        } else {
            GlobalDialog.createDialogwithtitle(this, getStringfromResource(R.string.internet_not_connected_msg));
        }

        networkre = new NetworkBroadcastReceiver(this);
        intentFilter = new IntentFilter(Actions.INTENT_FILTER_CONNECTIVITY_ACTION);
        this.registerReceiver(networkre, intentFilter);

        powerbroadcastReceiver = new PowerBroadcastReceiver();
        powerIntentFilter = new IntentFilter();
        powerIntentFilter.addAction(Actions.POWER_CONNECTED_ACTION);
        powerIntentFilter.addAction(Actions.POWRE_DISCONNECTED_ACTION);
        powerIntentFilter.addAction(Actions.BATTERY_CHANGED);
        powerIntentFilter.addAction(Actions.BATTERY_OKAY);
        powerIntentFilter.addAction(Actions.BATTERY_LOW);
        this.registerReceiver(powerbroadcastReceiver, powerIntentFilter);

        if (wifiManager.isWifiEnabled() == false) {
            GlobalDialog.createDialogwithtitle(this, getStringfromResource(R.string.wifi_on_msg));
            wifiManager.setWifiEnabled(true);
        }

        aBroadcastReceiver = new ABroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(aBroadcastReceiver,new IntentFilter("com.google.firebase.MESSAGING_EVENT"));

        wifiReceiver = new WifiReceiver();
        wifiintent = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, wifiintent);
        wifiManager.startScan();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int ItemID = menuItem.getItemId();
                Fragment fragment = null;
                switch (ItemID) {
                    case R.id.user:
                      File file  =  Environment.getRootDirectory();

                        fragment = new UsersFragment();
                        setFragment(fragment);
                        menuItem.setChecked(true);
                        toolbarSetting(getStringfromResource(R.string.usersList));
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.profile));
                        Intent intent1 = new Intent(Actions.ORDERED_BROADCAST_A);
                        Intent intent2 = new Intent(Actions.ORDERED_BROADCAST_B);
                        Intent intent3 = new Intent(Actions.ORDERED_BROADCAST_C);
                        //intent.putExtra("name","mohit");
                        sendOrderedBroadcast(intent3, null);
                        sendOrderedBroadcast(intent1, null);
                        sendOrderedBroadcast(intent2, null);
                        break;
                    case R.id.volley:
                        fragment = new VolleyFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.volley));
                        break;
                    case R.id.retrofit:
                        fragment = new RetrofitFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.retrofit));
                        break;
                    case R.id.okhttp:
                        fragment = new OkHttpFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.okHttp));
                        break;
                    case R.id.backgroundAsync:
                        fragment = new AsyncJsonFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.asyncTask));
                        break;
                    case R.id.jsonfile:
                        fragment = new JsonFileFragment();
                        setFragment(fragment);
                        toolbarSetting(getStringfromResource(R.string.jsonfile));
                        break;
                    case R.id.extra_thing:
                        Intent mExtraThing = new Intent(MainActivity.this, ExtraActivity.class);
                        startActivity(mExtraThing);
                        break;
                    case R.id.firebase_cloud_messaging:
                        Intent mFirebaseMessagEvent = new Intent(MainActivity.this, LogAppFirebaseMessagingEventActivity.class);
                        startActivity(mFirebaseMessagEvent);
                        break;
                    case R.id.alarm_manager:
                        Intent intent = new Intent(MainActivity.this,AlarmManagerActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        mShowDialoge();
                        break;
                }
                return true;
            }
        });

        Log.d(TAG, "onCreate: ");

    }


    public void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_loader, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logoutUser() {
        editor.putString(Constants.SHARED_PREFERENCE_USER_NAME, "");
        editor.apply();
    }

    public void openLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This is required to make the drawer toggle work
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mShowDialoge() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getStringfromResource(R.string.dialog_titile));
        builder.setMessage(getStringfromResource(R.string.dialog_msg));
        builder.setPositiveButton(getStringfromResource(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
                logoutUser();
                openLoginPage();
            }
        });
        builder.setNegativeButton(getStringfromResource(R.string.dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.create().show();
    }

    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        LocalBroadcastManager.getInstance(this).registerReceiver(aBroadcastReceiver,new IntentFilter("com.google.firebase.MESSAGING_EVENT"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkre, intentFilter);
        registerReceiver(powerbroadcastReceiver, powerIntentFilter);
        registerReceiver(wifiReceiver, wifiintent);
        LocalBroadcastManager.getInstance(this).registerReceiver(aBroadcastReceiver,new IntentFilter("com.google.firebase.MESSAGING_EVENT"));
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkre);
        unregisterReceiver(powerbroadcastReceiver);
        unregisterReceiver(wifiReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(aBroadcastReceiver);
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

    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> results = wifiManager.getScanResults();
                Log.d(TAG, "onReceive: " + results.size() + "");
            }
        }
    }

    public String getStringfromResource(int id) {
        return this.getResources().getString(id);
    }

    public void toolbarSetting(String toolbarText) {
        toolbar.setTitle(toolbarText);
        toolbar.setTitleTextColor(Color.parseColor(getStringfromResource(R.string.toolbar_text_color)));
        drawerLayout.closeDrawers();
    }

    @Override
    public void setResponse(String result) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }


}
