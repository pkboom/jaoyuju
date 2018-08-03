package com.sujichim.jasanjao2.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.MainActivity;
import com.sujichim.jasanjao2.R;
import com.sujichim.jasanjao2.Ungi;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by yedam on 15. 9. 28.
 * when adding a new column, add
 * 1. mEditText = (EditText) findViewById(R.id.editText_newcolumn)
 * mEditText.setText(record.getNewColumn()) in onCreate()
 * 2. mEditText = (EditText) findViewById(R.id.editText_newcolumn)
 * record.setNewColumn(mEditText.getText().toString()) in insertRecord()
 * 3. mEditText = (EditText) findViewById(R.id.editText_newcolumn)
 * record.setNewColumn(mEditText.getText().toString()) in updateRecord()
 * 4. mEditText = (EditText) findViewById(R.id.editText_newcolumn)
 * mEditText.setText(null) in copyRecord()
 */

public class Form extends AppCompatActivity {
    private static final String LOGTAG = "Form";

    boolean isNewRecord;
    //imageFolder needs to be deleted, when leaving Form without saving it.
    Record record;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_form);

        EditText mEditText;

        //get info from List.class
        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra("record");

        if (record == null) {
            //insert new record
            isNewRecord = true;
            String temp = intent.getExtras().getString("info");
            Log.i("info", temp);
            if ( temp != ""){
                mEditText = (EditText) findViewById(R.id.editTextMemo);
                mEditText.setText(intent.getExtras().getString("info"));
            }
        } else {
            //show record
            isNewRecord = false; //it's update

            //show item
            mEditText = (EditText) findViewById(R.id.editTextName);
            mEditText.setText(record.getName());

            mEditText = (EditText) findViewById(R.id.editTextMemo);
            mEditText.setText(record.getMemo());

//            hideKeyboard();
        }

        Button mButton = (Button) findViewById(R.id.button_save);
        assert mButton != null;
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //save imageFolder of current record
                        //Log.d(LOGTAG, "BottomFragment clicked");
                        if (isNewRecord) {
                            //insert new record
                            Toast.makeText(Form.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                            insertRecord();
                        } else {
                            //imageFolderDeletable is always false if it's update
                            //update record
                            Toast.makeText(Form.this, "업데이트되었습니다", Toast.LENGTH_SHORT).show();
                            updateRecord();
                        }
                    }
                }
        );

        mButton = (Button) findViewById(R.id.button_list);
        assert mButton != null;
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //Log.d(LOGTAG, "BottomFragment clicked");
                        Intent intent = new Intent(Form.this, List.class);
                        startActivity(intent);
                    }
                }
        );

        mButton = (Button) findViewById(R.id.button_home);
        assert mButton != null;
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //Log.d(LOGTAG, "BottomFragment clicked");
                        startActivity(new Intent(Form.this, MainActivity.class));
                    }
                }
        );

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void insertRecord() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String currentDate = dateFormat.format(cal.getTime());

//        Log.d(LOGTAG, "currentDate " + currentDate);

        // Inserting new record
        record = new Record();

        EditText mEditText = (EditText) findViewById(R.id.editTextName);
        record.setName(mEditText.getText().toString());

        mEditText = (EditText) findViewById(R.id.editTextMemo);
        record.setMemo(mEditText.getText().toString());

        record.setDate(currentDate);
        Log.d(LOGTAG, "Inserted");

        Intent intent = new Intent(this, List.class);
        intent.putExtra("record", record);
        intent.putExtra("form", "new");
        startActivity(intent);
    }

    private void updateRecord() {
        EditText mEditText = (EditText) findViewById(R.id.editTextName);
        record.setName(mEditText.getText().toString());

        mEditText = (EditText) findViewById(R.id.editTextMemo);
        record.setMemo(mEditText.getText().toString());

        // updating record
        Log.d(LOGTAG, "Updated");

        Intent intent = new Intent(this, List.class);
        intent.putExtra("record", record);
        intent.putExtra("form", "update");
        startActivity(intent);
    }

/*
    //with back key pressed, return to RecordList
    @Override
    public void onBackPressed() {
        Log.d(LOGTAG, "BackPressed");
        startActivity(new Intent(this, Ungi.class));
    }
*/

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
