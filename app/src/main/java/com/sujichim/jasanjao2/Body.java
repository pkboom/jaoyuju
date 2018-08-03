package com.sujichim.jasanjao2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Body extends AppCompatActivity implements View.OnClickListener {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1253642133922353~8199002621");
        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        Button btn = (Button) findViewById(R.id.buttonA);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonB);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonC);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonD);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonE);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonF);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonG);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonH);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonI);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonJ);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonK);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonL);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonM);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.buttonN);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), BodyPic.class);
        switch (v.getId()) {
            case R.id.buttonA:
                intent.putExtra("PAGE1", R.drawable.a1);
                intent.putExtra("PAGE2", R.drawable.a2);
                startActivity(intent);
                break;

            case R.id.buttonB:
                intent.putExtra("PAGE1", R.drawable.b1);
                intent.putExtra("PAGE2", R.drawable.b2);
                startActivity(intent);
                break;

            case R.id.buttonC:
                intent.putExtra("PAGE1", R.drawable.c1);
                intent.putExtra("PAGE2", R.drawable.c2);
                startActivity(intent);
                break;

            case R.id.buttonD:
                intent.putExtra("PAGE1", R.drawable.d1);
                intent.putExtra("PAGE2", R.drawable.d2);
                startActivity(intent);
                break;

            case R.id.buttonE:
                intent.putExtra("PAGE1", R.drawable.e1);
                intent.putExtra("PAGE2", R.drawable.e2);
                startActivity(intent);
                break;

            case R.id.buttonF:
                intent.putExtra("PAGE1", R.drawable.f1);
                intent.putExtra("PAGE2", R.drawable.f2);
                startActivity(intent);
                break;

            case R.id.buttonG:
                intent.putExtra("PAGE1", R.drawable.g1);
                intent.putExtra("PAGE2", R.drawable.g2);
                startActivity(intent);
                break;

            case R.id.buttonH:
                intent.putExtra("PAGE1", R.drawable.h1);
                intent.putExtra("PAGE2", R.drawable.h2);
                startActivity(intent);
                break;

            case R.id.buttonI:
                intent.putExtra("PAGE1", R.drawable.i1);
                intent.putExtra("PAGE2", R.drawable.i2);
                startActivity(intent);
                break;

            case R.id.buttonJ:
                intent.putExtra("PAGE1", R.drawable.j1);
                intent.putExtra("PAGE2", R.drawable.j2);
                startActivity(intent);
                break;

            case R.id.buttonK:
                intent.putExtra("PAGE1", R.drawable.k1);
                intent.putExtra("PAGE2", R.drawable.k2);
                startActivity(intent);
                break;

            case R.id.buttonL:
                intent.putExtra("PAGE1", R.drawable.l1);
                intent.putExtra("PAGE2", R.drawable.l2);
                startActivity(intent);
                break;

            case R.id.buttonM:
                intent.putExtra("PAGE1", R.drawable.m1);
                intent.putExtra("PAGE2", R.drawable.m2);
                startActivity(intent);
                break;

            case R.id.buttonN:
                intent.putExtra("PAGE1", R.drawable.n1);
                intent.putExtra("PAGE2", R.drawable.n2);
                startActivity(intent);
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
