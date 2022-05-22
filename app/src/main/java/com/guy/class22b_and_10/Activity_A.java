package com.guy.class22b_and_10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_A extends AppCompatActivity {

    MaterialTextView main_LBL_info;
    MaterialButton main_BTN_openB;
    MaterialButton main_BTN_startService;
    MaterialButton main_BTN_stopService;
    MaterialButton main_BTN_checkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_LBL_info = findViewById(R.id.main_LBL_info);
        main_BTN_openB = findViewById(R.id.main_BTN_openB);
        main_BTN_startService = findViewById(R.id.main_BTN_startService);
        main_BTN_stopService = findViewById(R.id.main_BTN_stopService);
        main_BTN_checkService = findViewById(R.id.main_BTN_checkService);

        main_BTN_openB.setOnClickListener(view -> openB());
        main_BTN_startService.setOnClickListener(view -> startService());
        main_BTN_stopService.setOnClickListener(view -> stopService());
        main_BTN_checkService.setOnClickListener(view -> checkService());
    }

    private BroadcastReceiver myRadio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(MyService.MyService_PROGRESS, -1);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    main_LBL_info.setText(progress + " downloaded " + Thread.currentThread().getName());
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(myRadio, new IntentFilter(MyService.MyService_100FM));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myRadio);
    }

    private void openB() {
        Intent intent = new Intent(this, Activity_B.class);
        startActivity(intent);
        finish();
    }

    private void checkService() {
        main_LBL_info.setText("" + isServiceRunning(this, MyService.class.getName()));
    }

    private void startService() {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(MyService.MyService_ACTION_START);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(MyService.MyService_ACTION_STOP);
        startService(intent);
    }

    private boolean isServiceRunning(Context context, String serviceClassName) {
        ActivityManager activityManager = ContextCompat.getSystemService(this, ActivityManager.class);
        for (ActivityManager.RunningServiceInfo runningService : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            runningService.service.getShortClassName().contains(serviceClassName);
            return true;
        }
        return false;
    }
}