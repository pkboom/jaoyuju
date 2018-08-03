package com.sujichim.jasanjao2.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class BokjinQuiz2 extends AppCompatActivity implements View.OnClickListener {
    int pos1 = 0;
    int pos2 = 0;
    int questionOnDisplay = 0;
    String strAnswer;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bokjin_quiz2);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        ArrayAdapter<CharSequence> adapter1;
        ArrayAdapter<CharSequence> adapter2;

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray6,
                R.layout.spinnerlayout);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(UngiQuiz.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos1 = position;
                Log.i("pos1", Integer.toString(pos1));

//                GoNext();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.spinnerArray7,
                R.layout.spinnerlayout);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos2 = position;
                Log.i("pos2", Integer.toString(pos2));

//                GoNext();

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
        String[] bokjintype1 = {
                "○ ● x ● ○",
                "● ○ x ● ○",
                "○ ● x ○ ●",
                "● ○ x ○ ●",
                "● ● x ● ●",
                "○ ○ x ○ ○",
                "○ ○ x ○ ○",
                "● ● x ● ●",
                "○ ● x ● ○",
                "○ ○ x ● ○",
                "○ ● x ○ ○",
                "● ● x ● ○",
                "○ ● x ● ●",
                "○ ○ x ● ●",
                "● ● x ○ ○",
                "● ○ x ● ○",
                "○ ● x ○ ●",
                "● ● x ○ ●",
                "● ○ x ● ●",
                "○ ○ x ○ ●",
        };
        String[] bokjintype2 = {
                "○",
                "○",
                "○",
                "○",
                "○",
                "○",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
                "●",
        };
        int min = 0;
        int max = 20;
        int rnd;

        //0~4
        Random r = new Random();
        do {
            //같은 문제 나오는 것 방지
            rnd = r.nextInt(max - min) + min;
        } while (questionOnDisplay == rnd);
        Log.i("questionOnDisplay", Integer.toString(questionOnDisplay));
        Log.i("rnd", Integer.toString(rnd));

        questionOnDisplay = rnd;

        TextView tV = (TextView) findViewById(R.id.textView1);
        tV.setText(bokjintype1[rnd]);
        tV = (TextView) findViewById(R.id.textView2);
        tV.setText(bokjintype2[rnd]);

//        Button btn = (Button) findViewById(R.id.button_input);
//        btn.setText("확 인");

        switch (questionOnDisplay) {
            case 0:
                strAnswer = "00";
                break;
            case 1:
                strAnswer = "02";
                break;
            case 2:
                strAnswer = "20";
                break;
            case 3:
            case 4:
            case 5:
                strAnswer = "22";
                break;
            case 6:
            case 7:
            case 8:
                strAnswer = "11";
                break;
            case 9:
            case 11:
                strAnswer = "01";
                break;
            case 10:
            case 12:
                strAnswer = "10";
                break;
            case 13:
            case 16:
            case 17:
            case 19:
                strAnswer = "21";
                break;
            case 14:
            case 15:
            case 18:
                strAnswer = "12";
                break;
            default:
                break;
        }
        Log.i("strAnswer", strAnswer);
    }

    private void GoNext() {
//        String[] jang = {"간", "심", "비", "폐", "신"};
//        String[] bu = {"담", "소장", "위", "대장", "방광"};
//        String[] strMoreLess2 = { "승", "허" };

        int answer1 = Integer.parseInt(strAnswer.substring(0, 1));
        int answer2 = Integer.parseInt(strAnswer.substring(1));
        Log.i("answer1", Integer.toString(answer1));
        Log.i("answer2", Integer.toString(answer2));

        if (pos1 == answer1 && pos2 == answer2) {
//            Toast.makeText(BokjinQuiz2.this, "정답입니다.", Toast.LENGTH_SHORT).show();
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
//            quizz();
        } else {
//            Toast.makeText(BokjinQuiz2.this, "틀렸습니다.", Toast.LENGTH_SHORT).show();
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
                //Button btn = (Button) findViewById(R.id.button_input);
//                Toast.makeText(UngiQuiz.this, btn.getText(), Toast.LENGTH_SHORT).show();
                //if (btn.getText() == "확 인") {
                GoNext();
                //} else {
                //if go next, 새로 시작
                //quizz();
                //}
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