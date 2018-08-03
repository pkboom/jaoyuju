package com.sujichim.jasanjao2.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.R;

public class MainQuiz extends AppCompatActivity implements View.OnClickListener {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);
        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);
        getSupportActionBar().setTitle("");

        Button btn = (Button) findViewById(R.id.button_ungi);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_sanghap);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_bokjin);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_macjin);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_gimac);
        btn.setOnClickListener(this);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ungi:
                startActivity(new Intent(this, UngiQuiz.class));
                break;

            case R.id.button_sanghap:
                startActivity(new Intent(this, SanghapQuiz.class));
                break;

            case R.id.button_bokjin:
                startActivity(new Intent(this, BokjinQuiz2.class));
                break;

            case R.id.button_macjin:
                startActivity(new Intent(this, MacJin.class));
                break;

            case R.id.button_gimac:
                startActivity(new Intent(this, GiMac.class));
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
