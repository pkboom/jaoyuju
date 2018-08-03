package com.sujichim.jasanjao2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class TimerSec2 extends AppCompatActivity implements OnClickListener {

    int sec = 9; //sec default = 9(100 sec)
    int min;

    private final static String TAG = "BroadcastService";
    private final static String TAG2 = "BroadcastService2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_sec2);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        // 반복타이머 세팅
        String[] values1 = new String[12];
        for (int i = 0; i < values1.length; i++) {
            values1[i] = Integer.toString((i + 1) * 10);
        }

        TextView tV = (TextView) findViewById(R.id.textView1);
        tV.setVisibility(View.INVISIBLE);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);

        np.setMinValue(0);
        np.setMaxValue(values1.length - 1);
        np.setDisplayedValues(values1);
        np.setWrapSelectorWheel(true);
        np.setValue(9);

        // 터치해서 입력 안되게 함.
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //once the scroll is rolled, this is run
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                // TODO Auto-generated method stub
                // Log.i("seconds", Integer.toString(newVal));
                sec = newVal;
                Log.i("min", "sec = newVal;");
            }
        });

        // 타이머 분 세팅
        String[] values2 = new String[60];
        for (int i = 0; i < values2.length; i++) {
            values2[i] = Integer.toString(i + 1);
        }

        tV = (TextView) findViewById(R.id.textView2);
        tV.setVisibility(View.INVISIBLE);

        np = (NumberPicker) findViewById(R.id.numberPicker2);

        np.setMinValue(0);
        np.setMaxValue(values2.length - 1);
        np.setDisplayedValues(values2);

        np.setWrapSelectorWheel(true);
        // 입력 안되게 함.
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                // TODO Auto-generated method stub
                // Log.i("seconds", Integer.toString(newVal));
                min = newVal;
                Log.i("min", "min = newVal;");
            }
        });

        Button btn = (Button) findViewById(R.id.button_timeron1);
        btn.setText("반복\n타이머시작");
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_timeron2);
        btn.setText("타이머시작");
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_timeron1:
                Button btn = (Button) findViewById(R.id.button_timeron1);
                if (btn.getText() == "반복\n타이머시작") {
                    Log.i(TAG, "반복타이머시작");
                    startTimer();
                } else {
                    stopTimerTask();
                }
                break;

            case R.id.button_timeron2:
                Button btn2 = (Button) findViewById(R.id.button_timeron2);
                if (btn2.getText() == "타이머시작") {
                    startTimer2();
                    Log.i(TAG2, "타이머시작");
                } else {
                    stopTimerTask2();
                }
                break;

            default:
                break;
        }
    }


    public void startTimer() {
        Toast.makeText(getApplicationContext(),
                ((sec + 1) * 10) + " 초마다 알람이 울립니다", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, BroadcastService.class);
        intent.putExtra("timer", (sec + 1) * 10);
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 1000, pIntent);

        TextView tV = (TextView) findViewById(R.id.textView1);
        tV.setVisibility(View.VISIBLE);

        Button btn = (Button) findViewById(R.id.button_timeron1);
        btn.setText("반복\n타이머정지");

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);
        np.setVisibility(View.INVISIBLE);
    }

    public void stopTimerTask() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastService.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pIntent);
        stopService(intent);

        Log.i(TAG, "Stopped service");

        Toast.makeText(getApplicationContext(), "반복타이머 정지", Toast.LENGTH_SHORT)
                .show();

        Button btn = (Button) findViewById(R.id.button_timeron1);
        btn.setText("반복\n타이머시작");

        TextView tV = (TextView) findViewById(R.id.textView1);
        tV.setText("");
        tV.setVisibility(View.INVISIBLE);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);
        np.setVisibility(View.VISIBLE);
    }

    public void startTimer2() {
        Toast.makeText(getApplicationContext(),
                (min + 1) + " 분후에 알람이 울립니다", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, BroadcastService2.class);
        intent.putExtra("timer", (min + 1));
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 1000, pIntent);

        TextView tV = (TextView) findViewById(R.id.textView2);
        tV.setVisibility(View.VISIBLE);

        Button btn = (Button) findViewById(R.id.button_timeron2);
        btn.setText("타이머정지");

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker2);
        np.setVisibility(View.INVISIBLE);
        Log.i(TAG2, "Started service");
    }

    public void stopTimerTask2() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastService2.class);
        PendingIntent service = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pIntent = service;
        alarmManager.cancel(pIntent);
        stopService(intent);

        Log.i(TAG2, "Stopped service");

        // stop the timer, if it's not already null
        Toast.makeText(getApplicationContext(), "타이머 정지", Toast.LENGTH_SHORT)
                .show();

        Button btn = (Button) findViewById(R.id.button_timeron2);
        btn.setText("타이머시작");

        TextView tV = (TextView) findViewById(R.id.textView2);
        tV.setText("");
        tV.setVisibility(View.INVISIBLE);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker2);
        np.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    private BroadcastReceiver br2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI2(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        registerReceiver(br2, new IntentFilter(BroadcastService2.COUNTDOWN_BR));
        Log.i(TAG, "Registered broadcast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broadcast receiver");
    }

    @Override
    public void onStop() {
        try {
//            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        stopService(new Intent(this, BroadcastService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {

        if (intent.getExtras() != null) {
            Button btn = (Button) findViewById(R.id.button_timeron1);
            TextView tV = (TextView) findViewById(R.id.textView1);

            if (btn.getText().equals("반복\n타이머시작")) {
                btn.setText("반복\n타이머정지");
                tV.setVisibility(View.VISIBLE);
                NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);
                np.setVisibility(View.INVISIBLE);
            }

            int countdown = intent.getIntExtra("countdown", 0);
//            int timer = intent.getIntExtra("countdown_timer", 1);
//            millisUntilFinished = (millisUntilFinished / 1000) % timer;
            tV.setText((countdown > 9 ? Integer.toString(countdown) : "0" + Integer.toString(countdown)));
            if (countdown < 1) { tV.setText("00"); }
        }
    }

    private void updateGUI2(Intent intent) {

        if (intent.getExtras() != null) {
            Button btn = (Button) findViewById(R.id.button_timeron2);
            TextView tV = (TextView) findViewById(R.id.textView2);

            if (btn.getText().equals("타이머시작")) {
                btn.setText("타이머정지");
                tV.setVisibility(View.VISIBLE);
                NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker2);
                np.setVisibility(View.INVISIBLE);
            }

            int countdown = intent.getIntExtra("countdown", 0);
            int countdownMin = countdown / 60;
            int countdownSec = countdown % 60;

            tV.setText((countdownMin > 9 ? Integer.toString(countdownMin) :
                    "0" + Integer.toString(countdownMin)) + ":" +
                    (countdownSec > 9 ? Integer.toString(countdownSec) :
                            "0" + Integer.toString(countdownSec)));
            Log.i(TAG2, "Countdown seconds remaining: " + countdown);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timersec2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.help) {
            showHowTo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHowTo() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 닫기
            }
        });
        alert.setMessage("* 알람 소리가 나지 않을 때는\n"
                + "'환경설정->소리->알람'에서 무음 설정을 변경하면 됩니다.");
        alert.show();
    }
}

