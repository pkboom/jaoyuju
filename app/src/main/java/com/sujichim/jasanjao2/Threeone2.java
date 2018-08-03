package com.sujichim.jasanjao2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Threeone2 extends AppCompatActivity implements OnClickListener {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threeone2);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        // create buttons
        Button btn = (Button) findViewById(R.id.button_yangil);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_sinsil);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_eumsil);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_bokjindo);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_8);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_yangil:
                startActivity(new Intent(this, Yangsil.class));
                break;

            case R.id.button_sinsil:
                startActivity(new Intent(this, Sinsil.class));
                break;

            case R.id.button_eumsil:
                startActivity(new Intent(this, Eumsil.class));
                break;

            case R.id.button_bokjindo:
                startActivity(new Intent(this, Bokjindo.class));
                break;

            case R.id.button_8:
                startActivity(new Intent(this, Eightholes.class));
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
