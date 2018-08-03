package com.sujichim.jasanjao2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by gun on 14. 12. 31.
 */
public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.sujichim.jasanjao2.TimerSec";
    Intent bi = new Intent(COUNTDOWN_BR);

    int timer, counter;

    @Override
    public void onCreate() {
        super.onCreate();
        counter = 0;
        Log.i(TAG, "BroadcastService.onCreate()");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Timer cancelled");
        counter = 0;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand run");
        timer = intent.getIntExtra("timer", 0);
        Log.i(TAG, "timer: " + timer);
        counter++;
        int countdown = timer - counter;
        Log.i(TAG, "Countdown seconds remaining: " + countdown);
        bi.putExtra("countdown", countdown);
        sendBroadcast(bi);

        if (countdown < 1) {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(
                    getApplicationContext(), notification);
            r.play();
            counter = 0;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
