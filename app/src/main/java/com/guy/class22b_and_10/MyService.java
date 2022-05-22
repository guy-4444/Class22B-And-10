package com.guy.class22b_and_10;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {

    public static final String MyService_100FM = "MyService_100FM";
    public static final String MyService_PROGRESS = "MyService_PROGRESS";

    public static final String MyService_ACTION_START = "MyService_ACTION_START";
    public static final String MyService_ACTION_STOP = "MyService_ACTION_STOP";


    private MediaPlayer player;

    private boolean isRunning = false;
    private int song = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("pttt", "MyService onStartCommand");

        String action = intent.getAction();
        if (action.equals(MyService_ACTION_START)) {
            if (!isRunning) {
                isRunning = true;
                new Thread(() -> doSomethingHeavy()).start();
                downloadSong();
            }
        } else if (action.equals(MyService_ACTION_STOP)) {
            stopSomething();
        }
        return START_STICKY;
    }

    private void doSomethingHeavy() {
        player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI );
        player.setLooping(true);
        player.start();
    }

    Runnable runnable;
    Handler handler;

    private void downloadSong() {


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("pttt", "Thread A= " + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++) {
                    //Log.d("pttt", "" + i);
                }
                song += 5;

                Intent intent = new Intent(MyService_100FM);
                intent.putExtra(MyService_PROGRESS, song);

                new Thread(() -> {
                    sendBroadcast(intent);
                    Log.d("pttt", "Thread B= " + Thread.currentThread().getName());
                }).start();

                if (song == 100) {
                    stopSomething();
                } else {
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private void stopSomething() {
        handler.removeCallbacks(runnable);

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        stopSelf();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d("pttt", "MyService stopService");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        Log.d("pttt", "MyService onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
