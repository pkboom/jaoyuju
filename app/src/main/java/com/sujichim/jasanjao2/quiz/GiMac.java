package com.sujichim.jasanjao2.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class GiMac extends AppCompatActivity implements View.OnClickListener {
    int questionOnDisplay = 0;
    int strAnswer;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gi_mac);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        Button btn = (Button) findViewById(R.id.button_input);
        btn.setOnClickListener(this);

        quizz();

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void quizz() {
        String[] arrayGiMac = {"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};

        // references to our images
        Integer[] mThumbIds = {
                R.drawable.rightback0, R.drawable.rightback1,
                R.drawable.rightback2, R.drawable.rightback3,
                R.drawable.rightback4, R.drawable.rightback5
        };

        Integer[] mThumbIds2 = {
                R.drawable.leftpalm0, R.drawable.leftpalm1,
                R.drawable.leftpalm2, R.drawable.leftpalm3,
                R.drawable.leftpalm4, R.drawable.leftpalm5
        };

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

        //문제
        questionOnDisplay = rnd;

        Button btn = (Button) findViewById(R.id.button_gimac);
        btn.setText(arrayGiMac[questionOnDisplay]);

//        btn = (Button) findViewById(R.id.button_input);
//        btn.setText("확 인");

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = null;
//        ImageAdapter adapter=new  ImageAdapter(mThumbIds, this);
//        gridview.setAdapter(new ImageAdapter(this));
//        gridview.setAdapter(adapter);

        //손그림 display
        switch (questionOnDisplay) {
            case 0: //c
            case 8: //k
            case 4: //g
            case 11: //n
            case 3: //f
            case 2: //e
                adapter=new ImageAdapter(mThumbIds2, this);
                break;
            case 9: //l
            case 5: //h

            case 1: //d
            case 10://m
            case 6: //i
            case 7: //j
                adapter=new ImageAdapter(mThumbIds, this);
                break;
            default:
                break;
        }
        gridview.setAdapter(adapter);

        //손그림 click
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String strPosition = null;
                switch (questionOnDisplay) {
                    case 0: //c
                    case 8: //k
                    case 4: //g
                    case 11: //n
                    case 3: //f
                    case 2: //e
                        switch (position) {
                            case 0:
                                strPosition = "폐기맥 4지내측";
                                strAnswer = 0;
                                break;
                            case 1:
                                strPosition = "심포기맥 4지가운데";
                                strAnswer = 8;
                                break;
                            case 2:
                                strPosition = "심기맥 4지외측";
                                strAnswer = 4;
                                break;
                            case 3:
                                strPosition = "간기맥 5지내측";
                                strAnswer = 11;
                                break;
                            case 4:
                                strPosition = "비기맥 5지가운데";
                                strAnswer = 3;
                                break;
                            case 5:
                                strPosition = "위기맥 5지외측";
                                strAnswer = 2;
                                break;
                            default:
                                break;

                        }
                        break;
                    case 9: //l
                    case 5: //h
                    case 1: //d
                    case 10://m
                    case 6: //i
                    case 7: //j
                        switch (position) {
                            case 0:
                                strPosition = "신기맥 5지외측";
                                strAnswer = 7;
                                break;
                            case 1:
                                strPosition = "방광기맥 5지가운데";
                                strAnswer = 6;
                                break;
                            case 2:
                                strPosition = "담기맥 5지내측";
                                strAnswer = 10;
                                break;
                            case 3:
                                strPosition = "대장기맥 4지외측";
                                strAnswer = 1;
                                break;
                            case 4:
                                strPosition = "소장기맥 4지가운데";
                                strAnswer = 5;
                                break;
                            case 5:
                                strPosition = "삼초기맥 4지내측";
                                strAnswer = 9;
                                break;
                            default:
                                break;

                        }
                        break;
                    default:
                        break;
                }
                TextView tV = (TextView) findViewById(R.id.textView4);
                tV.setText(strPosition);

//                Toast.makeText(GiMac.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//                v.setAlpha(0.5f);
            }
        });

//        Log.i("strAnswer", strAnswer);
    }

    private void GoNext() {
        Log.i("answer1", Integer.toString(strAnswer));

        if (questionOnDisplay == strAnswer) {
            Button btn = (Button) findViewById(R.id.button_input);
            btn.setText("정답입니다");
            TextView tV = (TextView) findViewById(R.id.textView4);
            tV.setText("");
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
