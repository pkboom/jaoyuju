package com.sujichim.jasanjao2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by gun on 14. 12. 31.
 */
public class BroadcastService2 extends Service {

    private final static String TAG = "BroadcastService2";

    public static final String COUNTDOWN_BR = "com.sujichim.jasanjao2.TimerMin";
    Intent bi = new Intent(COUNTDOWN_BR);

    MediaPlayer mMediaPlayer2;

    int counter;

    @Override
    public void onCreate() {
        super.onCreate();
        counter = 0;
        Log.i(TAG, "Starting timer...");
    }

    @Override
    public void onDestroy() {
        counter = 0;
        if (mMediaPlayer2 != null) {
            if (mMediaPlayer2.isPlaying()) {
                mMediaPlayer2.stop();
            }
            mMediaPlayer2.reset();
            mMediaPlayer2.release();
            mMediaPlayer2 = null;
        }
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand run");

        //timer: time set by alarm
        //counter increases until it is equal to timer
        //countdown: time remaining
        int timer = intent.getIntExtra("timer", 0);
        Log.i(TAG, "timer: " + timer + " minute(s)");
        counter++;
        int countdown = (timer * 60) - counter; //time remaining
        if (countdown > 0) {
            Log.i(TAG, "Countdown remaining: " + countdown / 60 + ":" + countdown % 60);
            bi.putExtra("countdown", countdown);
            sendBroadcast(bi);
        } else if (countdown == 0) {
            //alarm goes off
            try {
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mMediaPlayer2 = new MediaPlayer();
                mMediaPlayer2.setDataSource(getApplicationContext(), alert);
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mMediaPlayer2.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mMediaPlayer2.setLooping(true);
                    mMediaPlayer2.prepare();
                    mMediaPlayer2.start();
                }
            } catch (Exception e) {
            }
            bi.putExtra("countdown", countdown);
            sendBroadcast(bi);
            Log.i(TAG, "Timer finished");
        } else {
            Log.i(TAG, "Countdown remaining: 00:00");
            countdown = 0;
            bi.putExtra("countdown", countdown);
            sendBroadcast(bi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}
