package com.sujichim.jasanjao2.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class GiMac2 extends AppCompatActivity implements View.OnClickListener {
    int pos1 = 0;
    int pos2 = 0;
    int questionOnDisplay = 0;
    String strAnswer;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gi_mac);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        ArrayAdapter<CharSequence> adapter1;

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray11,
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
                pos2 = position;
                TextView tV = (TextView) findViewById(R.id.textView4);
                String strPosition = null;
//                tV.setText(Integer.toString(position));
                switch (questionOnDisplay) {
                    case 0: //c
                    case 8: //k
                    case 4: //g
                    case 11: //n
                    case 3: //f
                    case 2: //e
                        switch (position) {
                            case 0:
                                strPosition = "4지내측 폐기맥";
                                break;
                            case 1:
                                strPosition = "4지가운데 심포기맥";
                                break;
                            case 2:
                                strPosition = "4지외측 심기맥";
                                break;
                            case 3:
                                strPosition = "5지내측 간기맥";
                                break;
                            case 4:
                                strPosition = "5지가운데 비기맥";
                                break;
                            case 5:
                                strPosition = "5지외측 위기맥";
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
                                strPosition = "5지외측 신기맥";
                                break;
                            case 1:
                                strPosition = "5지가운데 방광기맥";
                                break;
                            case 2:
                                strPosition = "5지내측 담기맥";
                                break;
                            case 3:
                                strPosition = "4지외측 대장기맥";
                                break;
                            case 4:
                                strPosition = "4지가운데 소장기맥";
                                break;
                            case 5:
                                strPosition = "4지내측 삼초기맥";
                                break;
                            default:
                                break;

                        }
                        break;
                    default:
                        break;
                }
                tV.setText(strPosition);

//                Toast.makeText(GiMac.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//                v.setAlpha(0.5f);
            }
        });

        //앞자리가 그림 정답, 뒤자리 기맥이름 정답
        switch (questionOnDisplay) {
            case 0: //c
                strAnswer = "10";
                break;
            case 8: //k
                strAnswer = "8";
                break;
            case 4: //g
                strAnswer = "7";
                break;
            case 11: //n
                strAnswer = "6";
                break;
            case 3: //f
                strAnswer = "9";
                break;
            case 2: //e
                strAnswer = "3";
                break;
            case 9: //l
                strAnswer = "2";
                break;
            case 5: //h
                strAnswer = "1";
                break;
            case 1: //d
                strAnswer = "4";
                break;
            case 10://m
                strAnswer = "0";
                break;
            case 6: //i
                strAnswer = "5";
                break;
            case 7: //j
                strAnswer = "11";
                break;
            default:
                break;
        }
/*
        switch (questionOnDisplay) {
            case 0: //c
                strAnswer = "010";
                break;
            case 8: //k
                strAnswer = "18";
                break;
            case 4: //g
                strAnswer = "27";
                break;
            case 11: //n
                strAnswer = "36";
                break;
            case 3: //f
                strAnswer = "49";
                break;
            case 2: //e
                strAnswer = "53";
                break;
            case 9: //l
                strAnswer = "52";
                break;
            case 5: //h
                strAnswer = "41";
                break;
            case 1: //d
                strAnswer = "34";
                break;
            case 10://m
                strAnswer = "20";
                break;
            case 6: //i
                strAnswer = "15";
                break;
            case 7: //j
                strAnswer = "011";
                break;
            default:
                break;
        }
*/
        Log.i("strAnswer", strAnswer);
    }

    private void GoNext() {
        int answer1 = Integer.parseInt(strAnswer.substring(0, 1));
        int answer2 = Integer.parseInt(strAnswer.substring(1));
        Log.i("answer1", Integer.toString(answer1));
        Log.i("answer2", Integer.toString(answer2));
/*
        int answer1 = Integer.parseInt(strAnswer.substring(0, 1));
        int answer2 = Integer.parseInt(strAnswer.substring(1));
        Log.i("answer1", Integer.toString(answer1));
        Log.i("answer2", Integer.toString(answer2));
*/

        //pos2:그림위치, pos1=기맥이름선택값
        if (pos2 == answer1 && pos1 == answer2) {
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
