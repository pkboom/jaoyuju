package com.sujichim.jasanjao2.quiz;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class MacJin extends AppCompatActivity implements View.OnClickListener {
    int pos1 = 0;
    int questionOnDisplay = 0;
    String strAnswer;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_jin);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        ArrayAdapter<CharSequence> adapter1;

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray10,
                R.layout.spinnerlayout);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos1 = position;
                Log.i("pos1", Integer.toString(pos1));
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btn = (Button) findViewById(R.id.button_input);
        btn.setOnClickListener(this);

        quizz();

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void quizz() {
        String[] arrayMacJin = {"부돌1성평맥", "부돌2성평맥", "부돌3성평맥", "부돌1성조맥",
                "부돌2성조맥", "부돌3성조맥", "촌구1성평맥", "촌구2성평맥", "촌구3성평맥",
                "촌구1성조맥", "촌구2성조맥", "촌구3성조맥"};
        int min = 0;
        int max = 12;
        int rnd;

        //0~11
        Random r = new Random();
        do {
            //같은 문제 나오는 것 방지
            rnd = r.nextInt(max - min) + min;
        } while (questionOnDisplay == rnd);
        Log.i("questionOnDisplay", Integer.toString(questionOnDisplay));
        Log.i("rnd", Integer.toString(rnd));

        questionOnDisplay = rnd;

        Button btn = (Button) findViewById(R.id.button_macjin);
        btn.setText(arrayMacJin[questionOnDisplay]);

//        btn = (Button) findViewById(R.id.button_input);
//        btn.setText("확 인");

        switch (questionOnDisplay) {
            case 0:
                strAnswer = "0";
                break;
            case 1:
                strAnswer = "5";
                break;
            case 2:
                strAnswer = "3";
                break;
            case 3:
                strAnswer = "2";
                break;
            case 4:
                strAnswer = "1";
                break;
            case 5:
                strAnswer = "4";
                break;
            case 6:
                strAnswer = "6";
                break;
            case 7:
                strAnswer = "11";
                break;
            case 8:
                strAnswer = "9";
                break;
            case 9:
                strAnswer = "8";
                break;
            case 10:
                strAnswer = "7";
                break;
            case 11:
                strAnswer = "10";
                break;
            default:
                break;
        }
        Log.i("strAnswer", strAnswer);

    }

    private void GoNext() {
        int answer = Integer.parseInt(strAnswer);
        Log.i("answer1", Integer.toString(answer));

        if (pos1 == answer) {
            Button btn = (Button) findViewById(R.id.button_input);
            btn.setText("정답입니다");
            quizz();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Button btn = (Button) findViewById(R.id.button_input);
                    btn.setText("확 인");

                }
            }, 1000);
        } else {
            Button btn = (Button) findViewById(R.id.button_input);
            btn.setText("틀렸습니다");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Button btn = (Button) findViewById(R.id.button_input);
                    btn.setText("다시 확인");
                }
            }, 1000);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_input:
                GoNext();
                break;

            default:
                break;
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
