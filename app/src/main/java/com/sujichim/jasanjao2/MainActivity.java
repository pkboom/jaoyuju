/*내가 만든 java 파일은
 * MainActivity.java
 * Hands.java
 * MyReceiver.java
 * PageFragment.java 
 * 다른 것들은 이미지 zoom in and out 하기 위해
 * import 한 것들.
 */
package com.sujichim.jasanjao2;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.sujichim.jasanjao2.card.List;
import com.sujichim.jasanjao2.quiz.MainQuiz;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//import net.daum.adam.publisher.AdView;
//import net.daum.adam.publisher.impl.AdError;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends AppCompatActivity implements OnClickListener {
    int unOne = 0; // 대운 or 1운
    int giOne = 0; // 1기
    int moreless = 0; // 태과, 불급
    private String[] owhal = {"목", "화", "토", "금", "수"};
    private String[] jang = {"간", "심", "비", "폐", "신"};
    private String[] bu = {"담", "소장", "위", "대장", "방광"};
    private String[] strMoreLess = {"태과", "불급"};
    private String[] strMoreLess2 = {"승", "허"};
    String strChun = null;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        jaoyuju();

        // 이전 Notification 있을 경우 취소
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        nm.cancelAll();

        Button btn = (Button) findViewById(R.id.button_Jao);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_body);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_hands);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_threeone);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_Ungi);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_timer);
        btn.setOnClickListener(this);

/*
        btn = (Button) findViewById(R.id.button_UngiYear);
        btn.setOnClickListener(this);
*/

        btn = (Button) findViewById(R.id.button_List);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_Ochi);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_Quiz);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // case R.id.button_alarmon:
            // setAlarm();
            // break;
            //
            // case R.id.button_alarmoff:
            // releaseAlarm();
            // break;
            case R.id.button_Jao:
/*
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss(); // 닫기
                            }
                        });
                alert.setMessage(whatDay(strChun));
                alert.show();
*/
                startActivity(new Intent(this, UngiNow.class));
                break;

            case R.id.button_body:
                startActivity(new Intent(this, Body.class));
                break;

            case R.id.button_hands:
                startActivity(new Intent(this, Hands2.class));
                break;


            case R.id.button_threeone:
                startActivity(new Intent(this, Threeone2.class));
                break;

            case R.id.button_Ungi:
                // Toast.makeText(getApplicationContext(), "개발중",
                // Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Ungi.class));
                break;

            case R.id.button_timer:
                startActivity(new Intent(this, TimerSec2.class));
                break;

/*
            case R.id.button_UngiYear:
                startActivity(new Intent(this, UngiYear.class));
                break;
*/

            case R.id.button_List:
                startActivity(new Intent(this, List.class));
                break;

            case R.id.button_Ochi:
                startActivity(new Intent(this, Ochi.class));
                break;

            case R.id.button_Quiz:
                startActivity(new Intent(this, MainQuiz.class));
                break;

            default:
                break;
        }
    }

    // 홀수 짝수 확인
    boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

//	private void setAlarm() {
//		String strAlarm = getString(R.string.alarm_on);
//		Toast.makeText(getApplicationContext(), strAlarm, Toast.LENGTH_LONG)
//				.show();
//
//		// 상태바에 아이콘 생김
//		setStatusBarIcon(this, true);
//
//		// 한국이 일본보다 24분 늦다.
//		Calendar calendar = Calendar.getInstance();
////		calendar.add(Calendar.MINUTE, -24);
//
//		// 짝수이면 1시간 더하고 홀수이면 2시간 더한다.
//		if (isOdd(calendar.get(Calendar.HOUR_OF_DAY))) {
//			calendar.add(Calendar.HOUR_OF_DAY, 2);
//		} else {
//			calendar.add(Calendar.HOUR_OF_DAY, 1);
//		}
//
//		// 분과 초는 0으로 세팅.
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
//		// Toast.makeText(MainActivity.this, "적용시간 : " +
//		// formatter.format(calendar.getTime()), Toast.LENGTH_LONG).show();
//		Log.i("settingtime", formatter.format(calendar.getTime()));
//
//		Intent intent = new Intent(this, MyReceiver.class);
//		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent,
//				PendingIntent.FLAG_CANCEL_CURRENT);
//
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		// 2시간마다 알람 발생.
//		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//				calendar.getTimeInMillis(), 2 * 60 * 60 * 1000, pIntent);
//	}

//	private void releaseAlarm() {
//		String strAlarm = getString(R.string.alarm_off);
//		Toast.makeText(getApplicationContext(), strAlarm, Toast.LENGTH_SHORT)
//				.show();
//
//		setStatusBarIcon(this, false);
//
//		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//		Intent intent = new Intent(this, MyReceiver.class);
//		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent,
//				PendingIntent.FLAG_CANCEL_CURRENT);
//		alarmManager.cancel(pIntent);
//	}

    public void jaoyuju() {
        // 현재시간 출력
        Calendar cal = Calendar.getInstance();
        Calendar cal2014 = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd(E) a hh:mm");
        String strToday = formatter.format(cal.getTimeInMillis());

        // 한국이 일본보다 24분 느리다
        // cal.add(Calendar.MINUTE, -24);

        // 비교 시간은 2014년 1월1일로 세팅
        cal2014.set(2014, 0, 1);
        // String strToday2 = formatter.format(cal2014.getTimeInMillis());
        // Log.i("Today2014", strToday2);

        long diff = cal.getTimeInMillis() - cal2014.getTimeInMillis();
        Log.i("diffOld", Long.toString(diff));

        if ((diff % 10) == 1) {
            diff--;
        } else if ((diff % 10) == 2) {
            diff -= 2;
        } else if ((diff % 10) == 3) {
            diff -= 3;
        } else if ((diff % 10) == 9) {
            diff++;
        } else if ((diff % 10) == 8) {
            diff += 2;
        } else if ((diff % 10) == 7) {
            diff += 3;
        }

        Log.i("diffNew", Long.toString(diff));

        // intChun=천간, intJi=지지
        int intChun = (int) ((diff / (24 * 60 * 60 * 1000)) % 10);
        int intJiDay = (int) (((diff / (24 * 60 * 60 * 1000)) + 8) % 12);
        int intJi = cal.get(Calendar.HOUR_OF_DAY);
        Log.i("intChun", Integer.toString(intChun));
        Log.i("intJi", Integer.toString(intJi));
        Log.i("intJiDay", Integer.toString(intJiDay));
        Log.i("Today", strToday);

        String strJiHour = null;

        // 지간이 23시가 넘어가면 천간에 1을 더한다
        if (intJi == 23) {
            intChun = (intChun + 1) % 10;
            intJiDay = (intJiDay + 1) % 12;
        }
        // Toast.makeText(getApplicationContext(), Integer.toString(intJiDay),
        // Toast.LENGTH_SHORT).show();

        switch (intChun) {
            case 1:
                strChun = "계";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 L1";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 J1";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 E45";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 N3";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 D2";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 G11";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 I35";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 F7";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 M27";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 C5";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 H7";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 J1";
                }
                break;
            case 2:
                strChun = "갑";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 E45";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 N3";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 D2";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 G11";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 I35";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 F7";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 M27";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 C5";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 H7";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 K15";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 M32";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 F1";
                }
                break;
            case 3:
                strChun = "을";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 H2";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 C11";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 E40";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 J3";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 D6";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 N7";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 I33";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 G7";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 L2";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 N1";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 D1";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 G13";
                }
                break;
            case 4:
                strChun = "병";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 I38";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 F5";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 M28";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 C7";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 H6";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 J7";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 E38";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 K13";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 H1";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 C13";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 E44";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 J2";
                }
                break;
            case 5:
                strChun = "정";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 D5";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 N5";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 I34";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 G9";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 M26";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 F9";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 L5";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 G15";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 I39";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 F3";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 M31";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 C9";
                }
                break;
            case 6:
                strChun = "무";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 H5";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 J5";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 E39";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 N9";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 D7";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 K10";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 E45";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 J1";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 D2";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 N3";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 I35";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 G11";
                }
                break;
            case 7:
                strChun = "기";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 M27";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 F7";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 H7";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 C5";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 L6";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 F1";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 M32";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 C11";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 H2";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 J3";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 E40";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 N7";
                }
                break;
            case 8:
                strChun = "경";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 D6";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 G7";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 I33";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 K8";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 D1";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 N1";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 I38";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 G13";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 M28";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 F5";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 H6";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 C7";
                }
                break;
            case 9:
                strChun = "신";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 E38";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 J7";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 L7";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 C13";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 H1";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 J2";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 E44";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 N5";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 D5";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 G9";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 I34";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 F9";
                }
                break;
            case 10:
            case 0:
                strChun = "임";
                switch (intJi) {
                    case 23:
                    case 0:
                        strJiHour = " 子시 M26";
                        break;
                    case 1:
                    case 2:
                        strJiHour = " 丑시 K6";
                        break;
                    case 3:
                    case 4:
                        strJiHour = " 寅시 I39";
                        break;
                    case 5:
                    case 6:
                        strJiHour = " 卯시 G15";
                        break;
                    case 7:
                    case 8:
                        strJiHour = " 辰시 M31";
                        break;
                    case 9:
                    case 10:
                        strJiHour = " 巳시 F3";
                        break;
                    case 11:
                    case 12:
                        strJiHour = " 午시 H5";
                        break;
                    case 13:
                    case 14:
                        strJiHour = " 未시 C9";
                        break;
                    case 15:
                    case 16:
                        strJiHour = " 申시 E39";
                        break;
                    case 17:
                    case 18:
                        strJiHour = " 酉시 J5";
                        break;
                    case 19:
                    case 20:
                        strJiHour = " 戌시 D7";
                        break;
                    case 21:
                    case 22:
                        strJiHour = " 亥시 N9";
                }
        }
        // String[] strJi = { "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유",
        // "술", "해" };
        // String[] strChun = { "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
        // };
        String[] strChunChinese = {"壬", "癸", "甲", "乙", "丙", "丁", "戊", "己",
                "庚", "辛"};
        String[] strJiChinese = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申",
                "酉", "戌", "亥"};
        Button btn = (Button) findViewById(R.id.button_Jao);
        getUnOne((intChun + 8) % 10);
        getGiOne(intJiDay);
        // 삼합이면 장부가 서로 반대, 상극 상외이면 장부는 승허가 서로 같음
        if (unOne == giOne || unOne == (giOne + 4) % 5
                || unOne == (giOne + 6) % 5) {
            btn.setText(strToday + "\n" + strChunChinese[intChun]
                    + strJiChinese[intJiDay] + "일 " + strJiHour + "\n"
                    + owhal[unOne] + owhal[giOne] + strMoreLess[moreless]
                    + "\n" + jang[unOne] + strMoreLess2[moreless] + ", "
                    + bu[giOne] + strMoreLess2[(moreless + 1) % 2]);
        } else {
            btn.setText(strToday + "\n" + strChunChinese[intChun]
                    + strJiChinese[intJiDay] + "일 " + strJiHour + "\n"
                    + owhal[unOne] + owhal[giOne] + strMoreLess[moreless]
                    + "\n" + jang[unOne] + strMoreLess2[moreless] + ", "
                    + bu[giOne] + strMoreLess2[moreless]);
        }
        // Log.i("intJi", Integer.toString(intJi));
        // Log.i("intChun", Integer.toString(intChun));
		/*
		 * Toast.makeText(getApplicationContext(), strChun, Toast.LENGTH_LONG)
		 * .show();
		 */
    }

    public void getGiOne(int i) {
        // TODO Auto-generated method stub
        // 육기오행
        switch (i) {
            case 0: // 자
            case 12: // 자
            case 6: // 오
                giOne = 3; // 금
                break;
            case 1: // 축
            case 7: // 미
                giOne = 4; // 수
                break;
            case 2: // 인
            case 8: // 신
                giOne = 0; // 목
                break;
            case 3: // 묘
            case 9: // 유
                giOne = 1; // 화
                break;
            case 4: // 진
            case 10: // 술
                giOne = 2; // 토
                break;
            case 5: // 사
            case 11: // 해
                giOne = 1; // 화
        }
        // Toast.makeText(Ungi.this, Integer.toString(giOne),
        // Toast.LENGTH_SHORT).show();
    }

    public void getUnOne(int i) {
        // TODO Auto-generated method stub
        // 천간오행
        switch (i) {
            case 0: // 갑
            case 10: // 갑
            case 5: // 기
                unOne = 2; // 토
                break;
            case 1: // 을
            case 6: // 경
                unOne = 3; // 금
                break;
            case 2: // 병
            case 7: // 신
                unOne = 4; // 수
                break;
            case 3: // 정
            case 8: // 임
                unOne = 0; // 목
                break;
            case 4: // 무
            case 9: // 계
                unOne = 1; // 화
        }
        // 태과, 불급
        switch (i) {
            case 0: // 갑
            case 10: // 갑
            case 2: // 병
            case 4: // 무
            case 6: // 경
            case 8: // 임
                moreless = 0;
                break; // 태과
            case 1: // 을
            case 3: // 정
            case 5: // 기
            case 7: // 신
            case 9: // 계
                moreless = 1;
                break;
        }
    }

    public String whatDay(String temp) {
        String strAlert;
        Log.i("whatday?", temp);

        switch (temp) {
            case "계":
                strAlert = "계일\n" +
                        "11~1am: L1\n" +
                        "1~3: J1\n" +
                        "3~5: E45\n" +
                        "5~7: N3\n" +
                        "7~9: D2\n" +
                        "9~11: G11\n" +
                        "11~1pm: I35\n" +
                        "1~3: F7\n" +
                        "3~5: M27\n" +
                        "5~7: C5\n" +
                        "7~9: H7\n" +
                        "9~11: J1";
                break;

            case "갑":
                strAlert = "갑일\n" +
                        "11~1am: E45\n" +
                        "1~3: N3\n" +
                        "3~5: D2\n" +
                        "5~7: G11\n" +
                        "7~9: I35\n" +
                        "9~11: F7\n" +
                        "11~1pm: M27\n" +
                        "1~3: C5\n" +
                        "3~5: H7\n" +
                        "5~7: K15\n" +
                        "7~9: M32\n" +
                        "9~11: F1";
                break;

            case "을":
                strAlert = "을일\n" +
                        "11~1am: H2\n" +
                        "1~3: C11\n" +
                        "3~5: E40\n" +
                        "5~7: J3\n" +
                        "7~9: D6\n" +
                        "9~11: N7\n" +
                        "11~1pm: I33\n" +
                        "1~3: G7\n" +
                        "3~5: L2\n" +
                        "5~7: N1\n" +
                        "7~9: D1\n" +
                        "9~11: G13\n";
                break;

            case "병":
                strAlert = "병일\n" +
                        "11~1am: I38\n" +
                        "1~3: F5\n" +
                        "3~5: M28\n" +
                        "5~7: C7\n" +
                        "7~9: H6\n" +
                        "9~11: J7\n" +
                        "11~1pm: E38\n" +
                        "1~3: K13\n" +
                        "3~5: H1\n" +
                        "5~7: C13\n" +
                        "7~9: E44\n" +
                        "9~11: J2";
                break;

            case "정":
                strAlert = "정일\n" +
                        "11~1am: D5\n" +
                        "1~3: N5\n" +
                        "3~5: I34\n" +
                        "5~7: G9\n" +
                        "7~9: M26\n" +
                        "9~11: F9\n" +
                        "11~1pm: L5\n" +
                        "1~3: G15\n" +
                        "3~5: I39\n" +
                        "5~7: F3\n" +
                        "7~9: M31\n" +
                        "9~11: C9";
                break;

            case "무":
                strAlert = "무일\n" +
                        "11~1am: H5\n" +
                        "1~3: J5\n" +
                        "3~5: E39\n" +
                        "5~7: N9\n" +
                        "7~9: D7\n" +
                        "9~11: K10\n" +
                        "11~1pm: E45\n" +
                        "1~3: J1\n" +
                        "3~5: D2\n" +
                        "5~7: N3\n" +
                        "7~9: I35\n" +
                        "9~11: G11";
                break;

            case "기":
                strAlert = "기일\n" +
                        "11~1am: M27\n" +
                        "1~3: F7\n" +
                        "3~5: H7\n" +
                        "5~7: C5\n" +
                        "7~9: L6\n" +
                        "9~11: F1\n" +
                        "11~1pm: M32\n" +
                        "1~3: C11\n" +
                        "3~5: H2\n" +
                        "5~7: J3\n" +
                        "7~9: E40\n" +
                        "9~11: N7";
                break;

            case "경":
                strAlert = "경일\n" +
                        "11~1am: D6\n" +
                        "1~3: G7\n" +
                        "3~5: I33\n" +
                        "5~7: K8\n" +
                        "7~9: D1\n" +
                        "9~11: N1\n" +
                        "11~1pm: I38\n" +
                        "1~3: G13\n" +
                        "3~5: M28\n" +
                        "5~7: F5\n" +
                        "7~9: H6\n" +
                        "9~11: C7";
                break;

            case "신":
                strAlert = "신일\n" +
                        "11~1am: E38\n" +
                        "1~3: J7\n" +
                        "3~5: L7\n" +
                        "5~7: C13\n" +
                        "7~9: H1\n" +
                        "9~11: J2\n" +
                        "11~1: E44\n" +
                        "1~3: N5\n" +
                        "3~5: D5\n" +
                        "5~7: G9\n" +
                        "7~9: I34\n" +
                        "9~11: F9";
                break;

            case "임":
                strAlert = "임일\n" +
                        "11~1am: M26\n" +
                        "1~3: K6\n" +
                        "3~5: I39\n" +
                        "5~7: G15\n" +
                        "7~9: M31\n" +
                        "9~11: F3\n" +
                        "11~1pm: H5\n" +
                        "1~3: C9\n" +
                        "3~5: E39\n" +
                        "5~7: J5\n" +
                        "7~9: D7\n" +
                        "9~11: N9";
                break;

            default:
                return temp;
        }
        return strAlert;

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (adView != null) {
//            adView.destroy();
//            adView = null;
//        }
//    }
//
//    private void initAdam() {
//        // AdFit(Ad@m) sdk 초기화 시작
//        adView = (AdView) findViewById(R.id.adview);
//
//        // 광고 리스너 설정
//
//        // 1. 광고 클릭시 실행할 리스너
//        adView.setOnAdClickedListener(new AdView.OnAdClickedListener() {
//            @Override
//            public void OnAdClicked() {
//                Log.i(LOGTAG, "광고를 클릭했습니다.");
//            }
//        });
//
//        // 2. 광고 내려받기 실패했을 경우에 실행할 리스너
//        adView.setOnAdFailedListener(new AdView.OnAdFailedListener() {
//            @Override
//            public void OnAdFailed(AdError error, String message) {
//                Log.w(LOGTAG, message);
//            }
//        });
//
//        // 3. 광고를 정상적으로 내려받았을 경우에 실행할 리스너
//        adView.setOnAdLoadedListener(new AdView.OnAdLoadedListener() {
//            @Override
//            public void OnAdLoaded() {
//                Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
//            }
//        });
//
//        // 4. 광고를 불러올때 실행할 리스너
//        adView.setOnAdWillLoadListener(new AdView.OnAdWillLoadListener() {
//            @Override
//            public void OnAdWillLoad(String url) {
//                Log.i(LOGTAG, "광고를 불러옵니다. : " + url);
//            }
//        });
//
//
//        // 5. 광고를 닫았을때 실행할 리스너
//        adView.setOnAdClosedListener(new AdView.OnAdClosedListener() {
//            @Override
//            public void OnAdClosed() {
//                Log.i(LOGTAG, "광고를 닫았습니다.");
//            }
//        });
//
//
//        // 할당 받은 광고단위ID 설정
//        // adView.setClientId(“광고단위ID”);
//
//
//        // 광고 갱신 주기를 12초로 설정
//        // adView.setRequestInterval(12);
//
//
//        // 광고 영역에 캐시 사용 여부 : 기본 값은 true
//        adView.setAdCache(false);
//
//        // Animation 효과 : 기본 값은 AnimationType.NONE
//        adView.setAnimationType(AdView.AnimationType.FLIP_HORIZONTAL);
//        adView.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("TestAppActivity", "onRestart");

        // 이전 Notification 있을 경우 취소
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // return true;
        return super.onCreateOptionsMenu(menu);
    }

    private static void setStatusBarIcon(Context context, boolean enabled) {
        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED"); // intent
        // ��
        alarmChanged.putExtra("alarmSet", enabled); // intent�� ������ �߰� (true
        // => ������ ǥ��, false =>
        // ������ ����)
        context.sendBroadcast(alarmChanged); // intent�� Broadcast �Ѵ�.
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

        switch (id) {
            case R.id.sendemail:
                Intent it = new Intent(Intent.ACTION_SEND);
                String[] mailaddr = {"ppotpo@gmail.com"};

                it.setType("plain/text");
                it.putExtra(Intent.EXTRA_EMAIL, mailaddr); // 받는사람
                it.putExtra(Intent.EXTRA_SUBJECT, "건의사항"); // 제목
//                it.putExtra(Intent.EXTRA_TEXT, "건의사항이나 불편한 점이 있으시면 알려주세요.\n"); // 첨부내용

                startActivity(it);
                return true;

            case R.id.renew:
                jaoyuju();
                Toast.makeText(getApplicationContext(), "새로 고침", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
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
