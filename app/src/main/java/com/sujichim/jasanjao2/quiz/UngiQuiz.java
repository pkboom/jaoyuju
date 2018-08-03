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

public class UngiQuiz extends AppCompatActivity implements View.OnClickListener {
    int pos1, pos2, pos3, pos4, pos5, pos6;
    Spinner sp4, sp6;
    String strAnswer;
    int questionOnDisplay = 0;
    int answer1, answer2, answer3, answer4, answer5, answer6;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ungi_quiz);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        ArrayAdapter<CharSequence> adapter1;
        ArrayAdapter<CharSequence> adapter2;
        ArrayAdapter<CharSequence> adapter3;
        ArrayAdapter<CharSequence> adapter4;
        ArrayAdapter<CharSequence> adapter5;
        ArrayAdapter<CharSequence> adapter6;

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray8,
                R.layout.spinnerlayout);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(UngiQuiz.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos1 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.spinnerArray9,
                R.layout.spinnerlayout);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos2 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner sp3 = (Spinner) findViewById(R.id.spinner3);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.spinnerArray4,
                R.layout.spinnerlayout);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp3.setAdapter(adapter3);

        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos3 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp4 = (Spinner) findViewById(R.id.spinner4);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.spinnerArray4,
                R.layout.spinnerlayout);
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp4.setAdapter(adapter4);

        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos4 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner sp5 = (Spinner) findViewById(R.id.spinner5);
        adapter5 = ArrayAdapter.createFromResource(this, R.array.spinnerArray5,
                R.layout.spinnerlayout);
        adapter5.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp5.setAdapter(adapter5);

        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos5 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp6 = (Spinner) findViewById(R.id.spinner6);
        adapter6 = ArrayAdapter.createFromResource(this, R.array.spinnerArray5,
                R.layout.spinnerlayout);
        adapter6.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp6.setAdapter(adapter6);

        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos6 = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btn = (Button) findViewById(R.id.button_ungi1);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_input);
        btn.setOnClickListener(this);

        quizz();

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void quizz() {
        String[] owhal = {"목", "화", "토", "금", "수"};
        String[] strMoreLess = {"태과", "불급"};
        int min = 0;
        int max = 5;
        int Jang, Bu, MoreLess;

        //0~4
        Random r = new Random();
        do {
            //같은 문제 나오는 것 방지
            Jang = r.nextInt(max - min) + min;
        } while (questionOnDisplay == Jang);

        questionOnDisplay = Jang;
        Bu = r.nextInt(max - min) + min;

        //0~1
        max = 2;
        MoreLess = r.nextInt(max - min) + min;

        Log.i("Jang+Bu+MoreLess", Integer.toString(Jang) + Integer.toString(Bu) + Integer.toString(MoreLess));

        Button btn = (Button) findViewById(R.id.button_ungi1);
        btn.setText(owhal[Jang] + owhal[Bu] + strMoreLess[MoreLess]);

//        btn = (Button) findViewById(R.id.button_input);
//        btn.setText("확 인");

        // 삼합이면 장부가 서로 반대
        if (Jang == Bu || Jang == (Bu + 4) % 5 || Jang == (Bu + 6) % 5) {
            strAnswer = Integer.toString(Jang) + Integer.toString(MoreLess) + Integer.toString(Bu) + Integer.toString((MoreLess + 1) % 2);
        } else {
            //상극 상외이면 장부는 태과, 불급이 같음
            strAnswer = Integer.toString(Jang) + Integer.toString(MoreLess) + Integer.toString(Bu) + Integer.toString(MoreLess);
        }

        sp4.setVisibility(View.VISIBLE);
        sp6.setVisibility(View.VISIBLE);

        answer1 = Integer.parseInt(strAnswer.substring(0, 1))*2+Integer.parseInt(strAnswer.substring(1, 2));
        answer2 = Integer.parseInt(strAnswer.substring(2, 3))*2+Integer.parseInt(strAnswer.substring(3));

        switch (answer1) {
            //간승
            case 0:
                answer3 = 0;
                answer4 = 9;
                sp4.setVisibility(View.INVISIBLE);
                break;
            //간허
            case 1:
                answer3 = 2;
                answer4 = 3;
                break;
            //심승
            case 2:
                answer3 = 1;
                answer4 = 9;
                sp4.setVisibility(View.INVISIBLE);
                break;
            //심허
            case 3:
                answer3 = 3;
                answer4 = 4;
                break;
            //비승
            case 4:
                answer3 = 2;
                answer4 = 9;
                sp4.setVisibility(View.INVISIBLE);
                break;
            //비허
            case 5:
                answer3 = 4;
                answer4 = 0;
                break;
            //폐승
            case 6:
                answer3 = 3;
                answer4 = 9;
                sp4.setVisibility(View.INVISIBLE);
                break;
            //폐허
            case 7:
                answer3 = 0;
                answer4 = 1;
                break;
            //신승
            case 8:
                answer3 = 4;
                answer4 = 9;
                sp4.setVisibility(View.INVISIBLE);
                break;
            //신허
            case 9:
                answer3 = 1;
                answer4 = 2;
                break;
            default:
                break;
        }

        switch (answer2) {
            //담승
            case 0:
                answer5 = 0;
                answer6 = 9;
                sp6.setVisibility(View.INVISIBLE);
                break;
            //담허
            case 1:
                answer5 = 2;
                answer6 = 3;
                break;
            //소장승
            case 2:
                answer5 = 1;
                answer6 = 9;
                sp6.setVisibility(View.INVISIBLE);
                break;
            //소장
            case 3:
                answer5 = 3;
                answer6 = 4;
                break;
            //위승
            case 4:
                answer5 = 2;
                answer6 = 9;
                sp6.setVisibility(View.INVISIBLE);
                break;
            //위허
            case 5:
                answer5 = 4;
                answer6 = 0;
                break;
            //대장승
            case 6:
                answer5 = 3;
                answer6 = 9;
                sp6.setVisibility(View.INVISIBLE);
                break;
            //대장허
            case 7:
                answer5 = 0;
                answer6 = 1;
                break;
            //방광승
            case 8:
                answer5 = 4;
                answer6 = 9;
                sp6.setVisibility(View.INVISIBLE);
                break;
            //방광허
            case 9:
                answer5 = 1;
                answer6 = 2;
                break;
            default:
                break;
        }
        Log.i("jang+bu", Integer.toString(answer1)+Integer.toString(answer2));
        Log.i("more_jangbu", Integer.toString(answer3)+Integer.toString(answer4)+Integer.toString(answer5)+Integer.toString(answer6));
    }

    private void GoNext() {
        Log.i("input_jangbu", Integer.toString(pos1)+Integer.toString(pos2));
        Log.i("input_more_jangbu", Integer.toString(pos3)+Integer.toString(pos4)+Integer.toString(pos5)+Integer.toString(pos6));

        if ((answer1 % 2 == 0) && (answer2 % 2 == 0)) {
            //장부가 승일때
            Log.i("more more", "more_more");
            if ((pos1 == answer1 && pos2 == answer2) &&    //운기체형이 맞고
                    (pos3 == answer3) && (pos5 == answer5)) { //승증장부가 맞고
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
        } else if (answer1 % 2 == 0) {
            //장만 승일때
            Log.i("more less", "more less");
            if ((pos1 == answer1 && pos2 == answer2) &&    //운기체형이 맞고
                    (pos3 == answer3) &&  //승증5장이 맞고
                    (pos5 == answer5 || pos5 == answer6) &&
                    (pos6 == answer5 || pos6 == answer6) && //승증5부가 맞고
                    (pos5 != pos6)) {
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
        } else if (answer2 % 2 == 0) {
            //부만 승일때
            Log.i("less more", "less more");
            if ((pos1 == answer1 && pos2 == answer2) &&    //운기체형이 맞고
                    (pos3 == answer3 || pos3 == answer4) &&
                    (pos4 == answer3 || pos4 == answer4) &&  //승증5장이 맞고
                    (pos5 == answer5) && //승증5부가 맞고
                    (pos3 != pos4)) {
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

        } else {
            //장부가 허일때
            Log.i("less less", "less less");
            if ((pos1 == answer1 && pos2 == answer2) &&    //운기체형이 맞고
                    (pos3 == answer3 || pos3 == answer4) &&
                    (pos4 == answer3 || pos4 == answer4) &&  //승증5장이 맞고
                    (pos5 == answer5 || pos5 == answer6) &&
                    (pos6 == answer5 || pos6 == answer6) && //승증5부가 맞고
                    (pos3 != pos4) && (pos5 != pos6)) {
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
