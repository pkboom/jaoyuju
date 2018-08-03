package com.sujichim.jasanjao2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

public class UngiNow extends AppCompatActivity implements View.OnClickListener {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ungi_now);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        Button btn = (Button) findViewById(R.id.button11);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button21);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button31);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button41);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button51);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button12);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button22);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button32);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button42);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button52);
        btn.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //현재운기
        UngiCalc ungiTemp = new UngiCalc(this.getApplicationContext(), year, month, day, true);
        String temp = ungiTemp.getUngiLeft();
        btn = (Button) findViewById(R.id.button_top);
        btn.setOnClickListener(this);
        btn.setText(temp);

        //현재운기에 따른 장부 상태
        temp = ungiTemp.getJangbu(); //현재운기 장부 허승 파악
        //장부 허승으로 주변 장부 허승 파악
        UngiJangbu ungijangbu = new UngiJangbu(temp);
        String[] tempJangBu = ungijangbu.getJangStatus();

        ((Button) findViewById(R.id.button11)).setText(tempJangBu[0]);
        ((Button) findViewById(R.id.button21)).setText(tempJangBu[1]);
        ((Button) findViewById(R.id.button31)).setText(tempJangBu[2]);
        ((Button) findViewById(R.id.button41)).setText(tempJangBu[3]);
        ((Button) findViewById(R.id.button51)).setText(tempJangBu[4]);

        tempJangBu = ungijangbu.getBuStatus();

        ((Button) findViewById(R.id.button12)).setText(tempJangBu[0]);
        ((Button) findViewById(R.id.button22)).setText(tempJangBu[1]);
        ((Button) findViewById(R.id.button32)).setText(tempJangBu[2]);
        ((Button) findViewById(R.id.button42)).setText(tempJangBu[3]);
        ((Button) findViewById(R.id.button52)).setText(tempJangBu[4]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_top:
                startActivity(new Intent(this, UngiYear.class));
                break;

            case R.id.button11:
            case R.id.button21:
            case R.id.button31:
            case R.id.button41:
            case R.id.button51:
            case R.id.button12:
            case R.id.button22:
            case R.id.button32:
            case R.id.button42:
            case R.id.button52:
//                AlertDialog.Builder alert = new AlertDialog.Builder(UngiNow.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(UngiNow.this);
                alert.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss(); // 닫기
                            }
                        });
                String temp = "\uD83D\uDE0A: 해당 장부가 건강해지고 질병 회복도 빠릅니다.\n" +
                        "\uD83D\uDE1F: 해당 장부가 악화되기 쉽습니다. 관리가 필요합니다.";
                alert.setMessage(temp);
                alert.show();
                break;

            default:
                break;
        }
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
