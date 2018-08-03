package com.sujichim.jasanjao2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sujichim.jasanjao2.card.Form;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import jxl.Sheet;
import jxl.Workbook;

public class Ungi extends AppCompatActivity implements OnClickListener {
    // ChineseCalendar chineseCalendar;
    private String[][] julgi24 = new String[151][29];
    private String[] owhal = {"목", "화", "토", "금", "수"};
    private String[] jang = {"간", "심", "비", "폐", "신"};
    private String[] strMoreLess = {"태과", "불급"};
    private String resultUngi = null;
    private String threeOSix = null;

    int year, month, day;
    int yearSelect, monthSelect, daySelect;
    int yearLunar, monthLunar, dayLunar;
    int unOne = 0; // 대운 or 1운
    int giOne = 0; // 1기
    int moreless = 0; // 태과, 불급
    boolean plusMinus = true; // default = 양력
    boolean leftRight = true; // default = left, 좌측인지 우측인지 구분
    boolean ungi306 = false; // default = 묘 or 유날이 아님
    int isLeapMonth = 0; // default = 평달

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ungi);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        Calendar cal = Calendar.getInstance();

        copyExcelDataToDatabase();

        // 시작 날짜를 50년 정도 늦게 시작. 년도 찾는데 시간이 많이 걸린다고 해서
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        // year = 1964;
        // month = 1;
        // day = 24;

        // 우측 운기체형을 하면 선택된 날짜가 변경이 되어 미리 저장해둠.
        // 스마트폰 화면에 나타날 년 월 일 저장.
        yearSelect = yearLunar = year -= 50;
        monthSelect = monthLunar = month;
        daySelect = dayLunar = day;

        // TextView tV = (TextView) findViewById(R.id.textDate);
        // tV.setText(year + "년 " + (month + 1) + "월 " + day + "일 ");
        resultUngi = yearSelect + "년 " + (monthSelect + 1) + "월 " + daySelect
                + "일" + "\n";
        resultUngi += "좌";
//        leftUngi();
        UngiCalc ungiTemp = new UngiCalc(this.getApplicationContext(), year, month, day, leftRight);
        resultUngi += ungiTemp.getUngiBoth();

        resultUngi += "우";
        leftRight = false;
        rightUngi();

        // 묘나 유날인지 확인
        check306();

        // create buttons
        Button btn = (Button) findViewById(R.id.button_ungi);
        btn.setText(resultUngi);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_plus);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_minus);
        btn.setOnClickListener(this);

/*
        btn = (Button) findViewById(R.id.button_jisu);
        btn.setOnClickListener(this);
*/

        btn = (Button) findViewById(R.id.button_Card);
        btn.setOnClickListener(this);


//        btn = (Button) findViewById(R.id.button_screenshot);
//        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_share);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ungi:
                if (ungi306) {
                    // 입태 306일은 다른 레이아웃에서 보여줌
                    // Intent intent = new Intent(Ungi.this, Threeosix.class);
                    // intent.putExtra("another", threeOSix);
                    // startActivity(intent);

                    AlertDialog.Builder alert = new AlertDialog.Builder(Ungi.this);
                    alert.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss(); // 닫기
                                }
                            });
                    alert.setMessage("입태 306일\n" + threeOSix);
                    alert.show();
                }

                break;

            case R.id.button_plus:
                new DatePickerDialog(Ungi.this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, yearSelect,
                        monthSelect, daySelect).show();
                plusMinus = true;
                break;

            case R.id.button_minus:
                new DatePickerDialog(Ungi.this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, yearLunar,
                        monthLunar, dayLunar).show();
                plusMinus = false;
                DialogSelectOption();
                break;

            case R.id.button_share:
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, resultUngi);
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "운기체형"));
                break;

/*
            case R.id.button_jisu:
                startActivity(new Intent(this, JangbuJisu.class));
                break;
*/

            case R.id.button_Card:
                Intent mIntent = new Intent(this, Form.class);
                mIntent.putExtra("info", resultUngi + "\n");
                startActivity(mIntent);
                break;
        }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
        // rootView.setDrawingCacheEnabled(true);
        // return rootView.getDrawingCache();
    }

    //save screenshot to my device
    public void saveBitmap(Bitmap bitmap) {
        String mCurrentPhotoPath;
        int n = (int) (Math.random() * 10000) + 1;
        mCurrentPhotoPath = Environment.getExternalStorageDirectory()
                + "/pictures/screenshots/운기" + n + ".jpg";
        File imagePath = new File(mCurrentPhotoPath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        galleryAddPic(mCurrentPhotoPath);
    }

    //Add the Photo to a Gallery
    private void galleryAddPic(String str) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(str);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void check306() {
        // TODO Auto-generated method stub
        // Toast.makeText(Ungi.this,
        // Integer.toString(resultUngi.lastIndexOf(")")),
        // Toast.LENGTH_SHORT).show();
        if (ungi306) {
            threeOSix = resultUngi.substring(resultUngi.lastIndexOf("우"),
                    resultUngi.lastIndexOf(")") + 1);
            // threeOSix = resultUngi.substring(resultUngi.lastIndexOf("우"),
            // (resultUngi.lastIndexOf("n") - 1));
            // resultUngi = resultUngi.substring(0,
            // resultUngi.lastIndexOf("우"));
            resultUngi = resultUngi.substring(0,
                    resultUngi.lastIndexOf("우") - 2);
        } else {
            resultUngi = resultUngi.substring(0,
                    resultUngi.lastIndexOf(")") + 1);
        }
    }

    public void rightUngi() {

        // 1919/9/8을 기준일로 잡음.
        DateTime start = new DateTime(1919, 9, 8, 0, 0);
        DateTime end = new DateTime(year, month+1, day, 0, 0);

        int getDays = Days.daysBetween(start.withTimeAtStartOfDay() , end.withTimeAtStartOfDay()).getDays() + 6;
        getDays = getDays % 12;
        Log.i("getDays ", String.valueOf(getDays));

        if (getDays == 1 || getDays == 7) {
            end = end.plusDays(-275);

        } else if (getDays == 2 || getDays == 8) {
//            calSelect.add(Calendar.DATE, -265);
            end = end.plusDays(-265);

        } else if (getDays == 3 || getDays == 9) {
//            calSelect.add(Calendar.DATE, -255);
            end = end.plusDays(-255);

        } else if (getDays == 4 || getDays == 10) {
            ungi306 = true;
            if (getDays == 4) {
                resultUngi += "(卯)";
            } else {
                resultUngi += "(酉)";
            }
//            calSelect.add(Calendar.DATE, -245);
            end = end.plusDays(-245);

        } else if (getDays == 5 || getDays == 11) {
//            calSelect.add(Calendar.DATE, -295);
            end = end.plusDays(-295);

        } else if (getDays == 6 || getDays == 0) {
//            calSelect.add(Calendar.DATE, -285);
            end = end.plusDays(-285);

        }

//        year = calSelect.get(Calendar.YEAR);
//        month = calSelect.get(Calendar.MONTH);
//        day = calSelect.get(Calendar.DAY_OF_MONTH);
//        Log.i("conceived date  ", Integer.toString(year) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(day));
        year = end.getYear();
        month = end.getMonthOfYear();
        day = end.getDayOfMonth();
        Log.i("conceived date  ", Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day));

//

//        leftUngi();
        UngiCalc ungiTemp = new UngiCalc(this.getApplicationContext(), year, month-1, day, leftRight);
        resultUngi += ungiTemp.getUngiBoth();

        // 묘 혹은 유 날 일때 한번더 계산
        if (getDays == 4 || getDays == 10) {
//            calSelect.add(Calendar.DATE, -60);
            end = end.plusDays(-60);
//            year = calSelect.get(Calendar.YEAR);
//            month = calSelect.get(Calendar.MONTH);
//            day = calSelect.get(Calendar.DAY_OF_MONTH);

            // Toast.makeText(
            // Ungi.this,
            // "입태 " + Integer.toString(year) + "년 "
            // + Integer.toString(month + 1) + "월 "
            // + Integer.toString(day) + "일", Toast.LENGTH_LONG)
            // .show();

            if (getDays == 4) {
                resultUngi += "우(卯)";
            } else {
                resultUngi += "우(酉)";
            }

//            leftUngi();
            ungiTemp = new UngiCalc(this.getApplicationContext(), year, month, day, leftRight);
            resultUngi += ungiTemp.getUngiBoth();

        }
    }

    // 운기중에서 운
    public void leftUngi() {
        // TODO Auto-generated method stub
        Calendar cal = Calendar.getInstance();
        Calendar daehan = Calendar.getInstance();
        int samhap;
        String resultJangBu;
        String[] bu = {"담", "소장", "위", "대장", "방광"};
        String[] strMoreLess2 = {"승", "허"};
        String resultJisu = null;
        String temp = null;

        for (int nRow = 0; nRow < 151; nRow++) {
            if (year == Integer.parseInt(julgi24[nRow][0])) {
                temp = julgi24[nRow][1] + julgi24[nRow][3];
//                Log.i("selectedyear", temp);
                // 대운과 태급,불과 결정
                getUnOne(Integer.parseInt(julgi24[nRow][2]));

                // 선택된 날짜
                cal.set(year, month, day);

                switch (Integer.parseInt(julgi24[nRow][2])) {
                    case 1: // 갑
                    case 3: // 병
                    case 5: // 무
                    case 7: // 경
                    case 9: // 임
                        daehan = oun(julgi24[nRow][6], -13);
                        break;
                    case 2: // 을
                    case 4: // 정
                    case 6: // 기
                    case 8: // 신
                    case 10: // 계
                        daehan = oun(julgi24[nRow][6], 12);
                }

                if (temp.equals("무진")
                        || temp.equals("무술")
                        || temp.equals("경인")
                        || temp.equals("경신")
                        || temp.equals("경오")
                        || temp.equals("경자")
                        || temp.equals("기축")
                        || temp.equals("기미")
                        || temp.equals("을유")
                        || temp.equals("신해")
                        || temp.equals("계사")
                        || temp.equals("정묘")) {
                    daehan = oun(julgi24[nRow][6], 0);
                }

//                Log.i("daehan", String.valueOf(daehan.get(Calendar.YEAR))
//                        + "/" + String.valueOf(daehan.get(Calendar.MONTH)+1)
//                        + "/" + String.valueOf(daehan.get(Calendar.DAY_OF_MONTH)));


                if (cal.getTimeInMillis() < daehan.getTimeInMillis()) {
                    // 대한 이전(전년도 5운)
                    getUnOne(Integer.parseInt(julgi24[nRow - 1][2]));
                    unOne += 4;
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][11], -4)
                        .getTimeInMillis()) {
                    // 청명 4일전까지 = 1운
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][15], 1)
                        .getTimeInMillis()) {
                    // 망종 2일후까지 = 2운
                    unOne += 1;
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][19], 4)
                        .getTimeInMillis()) {
                    // 입추 5일후까지 = 3운
                    unOne += 2;
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][25], 7)
                        .getTimeInMillis()) {
                    // 입동 8일후까지 = 4운
                    unOne += 3;
                } else {
                    // 다음 해 대한이전까지 = 5운
                    unOne += 4;
                }
                resultUngi += " " + owhal[unOne % 5];
                resultJangBu = "(" + jang[unOne % 5] + strMoreLess2[moreless];
                resultJisu = "("
                        + jang[unOne % 5]
                        + " "
                        + (gujoJisu(jang[unOne % 5]) + ungiJisu(
                        jang[unOne % 5], strMoreLess2[moreless]));
                samhap = unOne % 5;

                // 운기중에서 기
                getGiOne(Integer.parseInt(julgi24[nRow][4]));
                if (cal.getTimeInMillis() < daehan.getTimeInMillis()) {
                    // 대한 이전(전년도 5운)
                    getGiOne(Integer.parseInt(julgi24[nRow - 1][4]));
                    giOne += 5;
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][10], -1)
                        .getTimeInMillis()) {
                    // 춘분전일까지 = 1기
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][14], -1)
                        .getTimeInMillis()) {
                    // 소만전일까지 = 2기
                    giOne += 1;
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][18], -1)
                        .getTimeInMillis()) {
                    // 대서전일까지 = 3기
                    giOne += 2;
                    // Toast.makeText(Ungi.this, Integer.toString(giOne),
                    // Toast.LENGTH_SHORT).show();
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][22], -1)
                        .getTimeInMillis()) {
                    // 추분전일까지 = 4기
                    giOne += 3;
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][26], -1)
                        .getTimeInMillis()) {
                    // 소설전일까지 = 5기
                    giOne += 4;
                } else {
                    // 다음 해 대한이전까지 = 6기
                    giOne += 5;
                }
                getGiOne(giOne);
                resultUngi += owhal[unOne] + strMoreLess[moreless] + "\n";

                // 삼합이면 장부가 서로 반대, 상극 상외이면 장부는 승허가 서로 같음
                if (samhap == unOne || samhap == (unOne + 4) % 5
                        || samhap == (unOne + 6) % 5) {
                    resultUngi += resultJangBu + ", " + bu[unOne]
                            + strMoreLess2[(moreless + 1) % 2] + ") \n";
                    resultJisu += ", "
                            + bu[unOne]
                            + " "
                            + (gujoJisu(bu[unOne]) + ungiJisu(bu[unOne],
                            strMoreLess2[(moreless + 1) % 2])) + ") \n";
                    resultUngi += resultJisu;
                } else {
                    resultUngi += resultJangBu + ", " + bu[unOne]
                            + strMoreLess2[moreless] + ") \n";
                    resultJisu += ", "
                            + bu[unOne]
                            + " "
                            + (gujoJisu(bu[unOne]) + ungiJisu(bu[unOne],
                            strMoreLess2[moreless])) + ") \n";
                    resultUngi += resultJisu;
                }
                break; // for 문에서 빠져 나감.
            }
        }
    }

    public double ungiJisu(String strJisu, String string1) {
        // TODO Auto-generated method stub
        double i = 0;

        switch (strJisu) {
            case "폐":
                i = 3.5;
                break;
            case "심":
                i = 3.5;
                break;
            case "심포":
                i = 3.5;
                break;
            case "간":
                i = 2.5;
                break;
            case "비":
                i = 2.5;
                break;
            case "신":
                i = 3.5;
                break;
            case "위":
                i = 2.5;
                break;
            case "담":
                i = 2.5;
                break;
            case "소장":
                i = 3.5;
                break;
            case "삼초":
                i = 3.5;
                break;
            case "대장":
                i = 3.5;
                break;
            case "방광":
                i = 2.5;
                break;
        }
        if (string1.equals("허")) {
            i = i * -1;
        }
        return i;
    }

    public int gujoJisu(String strJisu2) {
        // TODO Auto-generated method stub
        int i = 0;

        if (leftRight) {
            // 왼쪽
            switch (strJisu2) {
                case "폐":
                    i = -3;
                    break;
                case "심":
                    i = 3;
                    break;
                case "심포":
                    i = 3;
                    break;
                case "간":
                    i = 2;
                    break;
                case "비":
                    i = -2;
                    break;
                case "신":
                    i = -3;
                    break;
                case "위":
                    i = 2;
                    break;
                case "담":
                    i = -2;
                    break;
                case "소장":
                    i = -3;
                    break;
                case "삼초":
                    i = -3;
                    break;
                case "대장":
                    i = 3;
                    break;
                case "방광":
                    i = 2;
                    break;
            }
        } else {
            // 오른쪽
            switch (strJisu2) {
                case "폐":
                    i = 3;
                    break;
                case "심":
                    i = -3;
                    break;
                case "심포":
                    i = -3;
                    break;
                case "간":
                    i = 2;
                    break;
                case "비":
                    i = 1;
                    break;
                case "신":
                    i = 3;
                    break;
                case "위":
                    i = 2;
                    break;
                case "담":
                    i = 1;
                    break;
                case "소장":
                    i = 3;
                    break;
                case "삼초":
                    i = 3;
                    break;
                case "대장":
                    i = -3;
                    break;
                case "방광":
                    i = -2;
                    break;
            }

        }
        return i;
    }

    public void getGiOne(int i) {
        // TODO Auto-generated method stub
        // 육기오행
        switch (i) {
            case 1: // 자
            case 7: // 오
                giOne = 5; // 진
                unOne = 1; // 화
                break;
            case 2: // 축
            case 8: // 미
                giOne = 6; // 사
                unOne = 2; // 토
                break;
            case 3: // 인
            case 9: // 신
                giOne = 7; // 오
                unOne = 1; // 화
                break;
            case 4: // 묘
            case 10: // 유
                giOne = 2; // 축
                unOne = 3; // 금
                break;
            case 5: // 진
            case 11: // 술
                giOne = 3; // 인
                unOne = 4; // 수
                break;
            case 6: // 사
            case 12: // 해
                giOne = 4; // 묘
                unOne = 0; // 목
        }
        // Toast.makeText(Ungi.this, Integer.toString(giOne),
        // Toast.LENGTH_SHORT).show();
    }

    public void getUnOne(int i) {
        // TODO Auto-generated method stub
        // 천간오행
        switch (i) {
            case 1: // 갑
            case 6: // 기
                unOne = 2; // 토
                break;
            case 2: // 을
            case 7: // 경
                unOne = 3; // 금
                break;
            case 3: // 병
            case 8: // 신
                unOne = 4; // 수
                break;
            case 4: // 정
            case 9: // 임
                unOne = 0; // 목
                break;
            case 5: // 무
            case 10: // 계
                unOne = 1; // 화
        }
        // 태과, 불급
        switch (i) {
            case 1: // 갑
            case 3: // 병
            case 5: // 무
            case 7: // 경
            case 9: // 임
                moreless = 0;
                break; // 태과
            case 6: // 기
            case 2: // 을
            case 8: // 신
            case 4: // 정
            case 10: // 계
                moreless = 1;
                break;
        }
    }

    // 엑셀 셀에 있는 날짜를 Date 형식으로 바꿈
    public Calendar oun(String strDate, int i) {
        Calendar cal = Calendar.getInstance();
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // Date dateCompared = null;

        String strMonth = strDate.substring(0, strDate.indexOf("-"));
        String strDay = strDate.substring(strDate.indexOf("-") + 1);
        // Toast.makeText(Ungi.this, strMonth, Toast.LENGTH_SHORT).show();
        // Toast.makeText(Ungi.this, strDay, Toast.LENGTH_SHORT).show();

        cal.set(year, Integer.parseInt(strMonth) - 1, Integer.parseInt(strDay)
                + i);

//        Log.i("date", Integer.toString(cal.get(Calendar.YEAR)) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));
        // cal.set(year, Integer.parseInt(strMonth) - 1,
        // Integer.parseInt(strDay));
        // Toast.makeText(Ungi.this, strDate, Toast.LENGTH_SHORT).show();
        return cal;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year2, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // int yearLunar = 0;
            // int monthLunar = 0;
            // int dayLunar = 0;

            if (!plusMinus) {
                // 음력 -> 양력
                //year/month/day for display on calendar
                yearLunar = year2;
                monthLunar = monthOfYear;
                dayLunar = dayOfMonth;

                Log.i("solar  ", Integer.toString(year2) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(dayOfMonth));
                if (monthOfYear > 29) {
                    Log.i("date  ", "not valid");
                    Toast.makeText(Ungi.this, "존재하지 않는 날짜입니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                SolLun solLunInstance = new SolLun();
                String strDate = solLunInstance.LunToSol(year2, monthOfYear, dayOfMonth, isLeapMonth);
                Log.i("lunar  ", strDate);
                year2 = Integer.parseInt(strDate.substring(0,4));
                monthOfYear = Integer.parseInt(strDate.substring(4, 6))-1;
                dayOfMonth = Integer.parseInt(strDate.substring(6, 8));

//                ChineseCalendar cc = new ChineseCalendar();
//                Calendar cal = Calendar.getInstance();
//
//                cc.set(ChineseCalendar.EXTENDED_YEAR, year2 + 2637);
//                cc.set(ChineseCalendar.MONTH, monthOfYear);
//                cc.set(ChineseCalendar.DAY_OF_MONTH, dayOfMonth);
//                // 윤달인지 확인
//                cc.set(ChineseCalendar.IS_LEAP_MONTH, isLeapMonth);
//                if (cc.get(ChineseCalendar.IS_LEAP_MONTH) == 0
//                        && isLeapMonth == 1) {
////                    Log.i("isLeapMonth", Integer.toString(isLeapMonth));
//                    Toast.makeText(Ungi.this, "윤달이 아닙니다. 다시 선택해주세요.",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                cal.setTimeInMillis(cc.getTimeInMillis());
//                year2 = cal.get(Calendar.YEAR);
//                monthOfYear = cal.get(Calendar.MONTH);
//                dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                // String msg = String.format("%d / %d / %d", year2,
                // monthOfYear + 1, dayOfMonth);
            } else {
                Log.i("solar  ", Integer.toString(year2) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(dayOfMonth));
                SolLun solLunInstance = new SolLun();
                String strDate = solLunInstance.SolToLun(year2, monthOfYear, dayOfMonth);
                Log.i("lunar  ", strDate);
                Log.i("strDate.length  ", String.valueOf(strDate.length()));
                if (strDate.length() > 8) {
                    isLeapMonth = 1;
                }
                yearLunar = Integer.parseInt(strDate.substring(0,4));
                monthLunar = Integer.parseInt(strDate.substring(4, 6))-1;
                dayLunar = Integer.parseInt(strDate.substring(6, 8));
            }

            resultUngi = "";
            year = year2;
            month = monthOfYear;
            day = dayOfMonth;
            Log.i("y/m/d selected solar  ", Integer.toString(year) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(day));

//            resultUngi = "";
//            year = 1960;
//            month = 6;
//            day = 20;

            //year/month/day for display on calendar
            yearSelect = year;
            monthSelect = month;
            daySelect = day;

            if (isLeapMonth == 0) {
                //solar year
                resultUngi = year + "년 " + (month + 1) + "월 " + day + "일\n";
                resultUngi += "(음)" + yearLunar + "년 " + (monthLunar + 1) + "월 "
                        + dayLunar + "일\n";
            } else {
                resultUngi = year + "년 " + (month + 1) + "월 " + day + "일\n";
                resultUngi += "(음,윤)" + yearLunar + "년 " + (monthLunar + 1) + "월 "
                        + dayLunar + "일\n";
                isLeapMonth = 0;
            }

            getUngi();
        }
    };

    private void getUngi() {
        ungi306 = false;
        leftRight = true;
        resultUngi += "좌";
//            leftUngi();
        UngiCalc ungiTemp = new UngiCalc(this.getApplicationContext(), year, month, day, leftRight);
        resultUngi += ungiTemp.getUngiBoth();

        leftRight = false;
        resultUngi += "우";
        rightUngi();

        // 묘나 유날인지 확인
        check306();
        Button btn = (Button) findViewById(R.id.button_ungi);
        btn.setText(resultUngi);

        // TextView tV1 = (TextView) findViewById(R.id.textUngi);
        // tV1.setText(resultUngi);

    }

    private void DialogSelectOption() {
        final CharSequence[] items = {"평달", "윤달"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle("평달/윤달 선택") // 제목 설정
                .setItems(items, new DialogInterface.OnClickListener() { // 목록
                    // 클릭시
                    // 설정
                    public void onClick(DialogInterface dialog,
                                        int index) {
                        // Toast.makeText(getApplicationContext(),
                        // items[index], Toast.LENGTH_SHORT)
                        // .show();
                        // 윤달이면
                        if (index == 1) {
                            isLeapMonth = 1;
                        }
                    }
                });

        AlertDialog dialog = builder.create(); // 알림창 객체 생성
        dialog.show(); // 알림창 띄우기
    }

    public void copyExcelDataToDatabase() {
        //Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet;

        try {
            InputStream is = getBaseContext().getResources().getAssets()
                    .open("julgi24.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                sheet = workbook.getSheet(0);
                if (sheet != null) {

                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(1).length;
                    int nColumnStartIndex = 0;
                    int nColumnEndIndex = sheet.getRow(1).length;
                    //Log.i("nRowEndIndex", Integer.toString(nRowEndIndex));
                    //Log.i("nColumnEndIndex", Integer.toString(nColumnEndIndex));

                    for (int nRow = nRowStartIndex; nRow < nRowEndIndex; nRow++) {
                        for (int nCol = nColumnStartIndex; nCol < nColumnEndIndex; nCol++) {
                            julgi24[nRow][nCol] = sheet.getCell(nCol, nRow)
                                    .getContents();
//                            Log.i("Julgi", sheet.getCell(nCol, nRow).getContents());
                        }
                    }
                } else {
                    System.out.println("Sheet is null!!");
                }
            } else {
                System.out.println("WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ungi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
