package com.sujichim.jasanjao2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class Ochi extends AppCompatActivity implements View.OnClickListener {
    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;
    int pos1 = 0, pos2 = 0;
    //세로: 간 심 심포 비 폐 신 담 소 삼초 위 대 방
    //가로: 정방 승방 한방 열방 풍방 습방 조방 수방
    String[][] ochipoint = new String[][]{
            {"-: C7  +: N9 J7", "-: N3 G13 +: C7", "-: N9 J7 +: N3 G13", "-: N3 G13 +: N9 J7", "-: N1 G13 +: N7 C7", "-: N5 F5 +: N1 J7", "-: N7 C7 +: N3 G13", "-: N9 J7 +: N5 F5"},
            {"-: J7  +: G15 N1", "-: G11 F5 +: J7", "-: G7 J7 +: G13 F5", "-: G13 F5 +: G7 J7", "-: G15 N1 +: G9 C7", "-: G11 F5 +: G15 N1", "-: G9 C7 +: G13 N1", "-: G7 J7 +: G11 F5"},
            {"-: J7  +: K15 N1", "-: K10 F5 +: J7", "-: K6 J7 +: K13 F5", "-: K13 F5 +: K6 J7", "-: K15 N1 +: K8 C7", "-: K10 F5 +: K15 N1", "-: K8 C7 +: K13 N1", "-: K6 J7 +: K10 F5"},
            {"-: N1  +: F3 G13", "-: F7 C7 +: N1", "-: F9 J7 +: F3 G13", "-: F3 G13 +: F9 J7", "-: F1 N1 +: F7 C7", "-: F5 C7 +: F1 N1", "-: F7 C7 +: F3 G13", "-: F9 J7 +: F5 G13"},
            {"-: G13 +: C9 F5", "-: C5 J7 +: G13", "-: C5 J7 +: C11 G13", "-: C11 G13 +: C5 J7", "-: C13 N1 +: C7 F5", "-: C9 F5 +: C13 N1", "-: C7 J7 +: C11 G13", "-: C5 J7 +: C9 F5"},
            {"-: F5  +: J5 C7", "-: J1 N1 +: F5", "-: J7 C7 +: J2 G13", "-: J2 G13 +: J7 C7", "-: J1 N1 +: J5 C7", "-: J3 F5 +: J1 N1", "-: J5 C7 +: J2 G13", "-: J7 N1 +: J3 F5"},
            {"-: D1  +: M31 I38", "-: M27 H6 +: D1", "-: M31 I38 +: M27 H6", "-: M27 H6 +: M31 I38", "-: M28 H6 +: M32 D1", "-: M26 E38 +: M28 I38", "-: M32 D1 +: M27 H6", "-: M31 I38 +: M26 E38"},
            {"-: I38 +: H5 M28", "-: H7 E38 +: I38", "-: H2 I38 +: H6 E38", "-: H6 E38 +: H2 I38", "-: H5 M28 +: H1 D1", "-: H7 E38 +: H5 M28", "-: H1 D1 +: H6 M28", "-: H2 I38 +: H7 E38"},
            {"-: I38 +: L5 M28", "-: L7 E38 +: I38", "-: L2 I38 +: L6 E38", "-: L6 E38 +: L2 I38", "-: L5 M28 +: L1 D1", "-: L7 E38 +: L5 M28", "-: L1 D1 +: L6 M28", "-: L2 I38 +: L7 E38"},
            {"-: M28 +: E39 H6", "-: E45 D1 +: M28", "-: E44 I38 +: E39 H6", "-: E39 H6 +: E44 I38", "-: E40 M28 +: E45 D1", "-: E38 D1 +: E40 M28", "-: E45 D1 +: E39 H6", "-: E44 I38 +: E38 H6"},
            {"-: H6  +: D7 E38", "-: D2 I38 +: H6", "-: D2 I38 +: D6 H6", "-: D6 H6 +: D2 I38", "-: D5 M28 +: D1 E38", "-: D7 E38 +: D5 M28", "-: D1 I38 +: D6 H6", "-: D2 I38 +: D7 E38"},
            {"-: E38 +: I39 D1", "-: I35 M28 +: E38", "-: I38 D1 +: I34 H6", "-: I34 H6 +: I38 D1", "-: I35 M28 +: I39 D1", "-: I33 E38 +: I35 M28", "-: I39 D1 +: I34 H6", "-: I38 M28 +: I33 E38"}
    };
/*
    String[][] ochipoint = new String[][]{
            {"-: N7 C7 +: N9 J7", "-: N3 G13 +: C7", "-: N9 J7 +: N3 G13", "-: N3 G13 +: N9 J7", "-: N1 G13 +: N7 C7", "-: N5 F5 +: N1 J7", "-: N7 C7 +: N3 G13", "-: N9 J7 +: N5 F5"},
            {"-: G7 J7 +: G15 N1", "-: G11 F5 +: G7 J7", "-: G7 J7 +: G13 F5", "-: G13 F5 +: G7 J7", "-: G15 N1 +: G9 C7", "-: G11 F5 +: G15 N1", "-: G9 C7 +: G13 N1", "-: G7 J7 +: G11 F5"},
            {"-: K6 J7 +: K15 N1", "-: K10 F5 +: K6 J7", "-: K6 J7 +: K13 F5", "-: K13 F5 +: K6 J7", "-: K15 N1 +: K8 C7", "-: K10 F5 +: K15 N1", "-: K8 C7 +: K13 N1", "-: K6 J7 +: K10 F5"},
            {"-: F1(F2) N1 +: F3 G13", "-: F7 C7 +: F1 N1", "-: F9 J7 +: F3 G13", "-: F3 G13 +: F9 J7", "-: F1 N1 +: F7 C7", "-: F5 C7 +: F1 N1", "-: F7 C7 +: F3 G13", "-: F9 J7 +: F5 G13"},
            {"-: G13 +: C9 F5", "-: C5 J7 +: G13", "-: C5 J7 +: C11 G13", "-: C11 G13 +: C5 J7", "-: C13 N1 +: C7 F5", "-: C9 F5 +: C13 N1", "-: C7 J7 +: C11 G13", "-: C5 J7 +: C9 F5"},
            {"-: J3 F5 +: J5 C7", "-: J1 N1 +: F5", "-: J7 C7 +: J2 G13", "-: J2 G13 +: J7 C7", "-: J1 N1 +: J5 C7", "-: J3 F5 +: J1 N1", "-: J5 C7 +: J2 G13", "-: J7 N1 +: J3 F5"},
            {"-: D1 +: M31 I38", "-: M27 H6 +: D1", "-: M31 I38 +: M27 H6", "-: M27 H6 +: M31 I38", "-: M28 H6 +: M32 D1", "-: M26 E38 +: M28 I38", "-: M32 D1 +: M27 H6", "-: M31 I38 +: M26 E38"},
            {"-: H2 I38 +: H5 M28", "-: H7 E38 +: H2 I38", "-: H2 I38 +: H6 E38", "-: H6 E38 +: H2 I38", "-: H5 M28 +: H1 D1", "-: H7 E38 +: H5 M28", "-: H1 D1 +: H6 M28", "-: H2 I38 +: H7 E38"},
            {"-: L2 I38 +: L5 M28", "-: L7 E38 +: L2 I38", "-: L2 I38 +: L6 E38", "-: L6 E38 +: L2 I38", "-: L5 M28 +: L1 D1", "-: L7 E38 +: L5 M28", "-: L1 D1 +: L6 M28", "-: L2 I38 +: L7 E38"},
            {"-: M28 +: E39 H6", "-: E45 D1 +: E40 M28", "-: E44 I38 +: E39 H6", "-: E39 H6 +: E44 I38", "-: E40 M28 +: E45 D1", "-: E38 D1 +: E40 M28", "-: E45 D1 +: E39 H6", "-: E44 I38 +: E38 H6"},
            {"-: D6 H6 +: D7 E38", "-: D2 I38 +: H6", "-: D2 I38 +: D6 H6", "-: D6 H6 +: D2 I38", "-: D5 M28 +: D1 E38", "-: D7 E38 +: D5 M28", "-: D1 I38 +: D6 H6", "-: D2 I38 +: D7 E38"},
            {"-: I33 E38 +: I39 D1", "-: I35 M28 +: I33 E38", "-: I38 D1 +: I34 H6", "-: I34 H6 +: I38 D1", "-: I35 M28 +: I39 D1", "-: I33 E38 +: I35 M28", "-: I39 D1 +: I34 H6", "-: I38 M28 +: I33 E38"}
    };
*/

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ochi);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray1,
                R.layout.spinnerlayout);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter1.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos1 = position;
                ochibang();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // spinner2
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.spinnerArray2,
                R.layout.spinnerlayout);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Ochi.this,
//                        adapter2.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                pos2 = position;
                ochibang();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btn = (Button) findViewById(R.id.button_Ochi);
        btn.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Ochi:
                ochi_exception();
                break;

            default:
                break;
        }
    }

    private void ochi_exception() {
        if (pos1 == 0 && pos2 == 1) {
            ochi_exception_show(ochipoint[pos1][pos2], "N7");

        } else if (pos1 == 4 && pos2 == 0) {
            ochi_exception_show2(ochipoint[pos1][pos2], "C11");

        } else if (pos1 == 4 && pos2 == 1) {
            ochi_exception_show(ochipoint[pos1][pos2], "C11");

        } else if (pos1 == 5 && pos2 == 1) {
            ochi_exception_show(ochipoint[pos1][pos2], "J3");

        } else if (pos1 == 6 && pos2 == 0) {
            ochi_exception_show2(ochipoint[pos1][pos2], "M32");

        } else if (pos1 == 6 && pos2 == 1) {
            ochi_exception_show(ochipoint[pos1][pos2], "M32");

        } else if (pos1 == 9 && pos2 == 0) {
            ochi_exception_show2(ochipoint[pos1][pos2], "E40");

        } else if (pos1 == 10 && pos2 == 1) {
            ochi_exception_show(ochipoint[pos1][pos2], "D6");

        }
    }

    //'사'에 역작용 있을 때
    private void ochi_exception_show2(String temp, String temp2) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Ochi.this);
        alert.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss(); // 닫기
                    }
                });

        temp = temp.substring(0, temp.lastIndexOf("+") - 1) + " " + temp2 + "(역작용)" + '\n' + temp.substring(temp.lastIndexOf("+"));
        alert.setMessage(temp);
        alert.show();

    }

    //'보'에 역작용 있을 때
    private void ochi_exception_show(String temp, String temp2) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Ochi.this);
        alert.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss(); // 닫기
                    }
                });

        temp = temp.substring(0, temp.lastIndexOf("+") - 1) + '\n' + temp.substring(temp.lastIndexOf("+"));
        temp = temp + " " + temp2 + "(역작용)";
        alert.setMessage(temp);
        alert.show();
    }


    private void ochibang() {
        String temp = ochipoint[pos1][pos2];
        temp = temp.substring(0, temp.lastIndexOf("+") - 1) + '\n' + temp.substring(temp.lastIndexOf("+"));
//        Toast.makeText(Ochi.this, Integer.toString(temp.substring(0, temp.lastIndexOf("+") - 2).length()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Ochi.this, temp.substring(0, temp.lastIndexOf("+")-2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Ochi.this, Integer.toString(temp.substring(temp.lastIndexOf("+")).length()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Ochi.this, temp.substring(temp.lastIndexOf("+")), Toast.LENGTH_SHORT).show();
        Button btn = (Button) findViewById(R.id.button_Ochi);
        btn.setText(temp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ochi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
