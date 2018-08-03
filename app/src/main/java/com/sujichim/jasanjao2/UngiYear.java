package com.sujichim.jasanjao2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jxl.Sheet;
import jxl.Workbook;

public class UngiYear extends AppCompatActivity implements OnClickListener {
    private static final String LOGTAG = "BannerTypeXML1";

    int year, month, day;
    int unOne = 0; // 대운 or 1운
    int giOne = 0; // 1기
    int moreless = 0; // 태과, 불급
    int yearSelect;
    private String[][] julgi24 = new String[151][29];
    private String[] owhal = {"목", "화", "토", "금", "수"};
    private String[] ungiDate = {"1-5", "3-1", "3-25", "5-1", "6-1", "7-1",
            "8-1", "9-1", "10-15", "11-18", "12-15"};
    private String[] strMoreLess = {"태과", "불급"};
    Calendar daehan = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ungi_year);

        //icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.hemark);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        copyExcelDataToDatabase();

        ungiSelectYear();

        Button btn = (Button) findViewById(R.id.button_lastyear);
        btn.setOnClickListener(this);

        btn = (Button) findViewById(R.id.button_nextyear);
        btn.setOnClickListener(this);

        // start daum ad@m
        //initAdam();
    }

    public void ungiSelectYear() {
        // TODO Auto-generated method stub
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String strUngi;

        TextView tV = (TextView) findViewById(R.id.textUngiYearTitle);
        tV.setText(year + "년 " + daehanSelect() + "년");

        // daehanSelect();

        // 5운 6기마다 장부허승 결정
        strUngi = "01/01~"
                + formatter.format(daehan.getTimeInMillis() - 24 * 60 * 60
                * 1000) + " ";
        strUngi += ungiOneYear(oun(ungiDate[0], 0)) + "\n";
        // 1운 1기
        strUngi += formatter.format(daehan.getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][10], -1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[1], 0)) + "\n";
        // 1운 2기
        strUngi += formatter.format(oun(julgi24[yearSelect][10], 0)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][11], -4)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[2], 0)) + "\n";
        // 2운 2기
        strUngi += formatter.format(oun(julgi24[yearSelect][11], -3)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][14], -1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[3], 0)) + "\n";
        // 2운 3기
        strUngi += formatter.format(oun(julgi24[yearSelect][14], 0)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][15], 1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[4], 0)) + "\n";
        // 3운 3기
        strUngi += formatter.format(oun(julgi24[yearSelect][15], 2)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][18], -1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[5], 0)) + "\n";
        // 3운 4기
        strUngi += formatter.format(oun(julgi24[yearSelect][18], 0)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][19], 4)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[6], 0)) + "\n";
        // 4운 4기
        strUngi += formatter.format(oun(julgi24[yearSelect][19], 5)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][22], -1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[7], 0)) + "\n";
        // 4운 5기
        strUngi += formatter.format(oun(julgi24[yearSelect][22], 0)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][25], 7)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[8], 0)) + "\n";
        // 5운 5기
        strUngi += formatter.format(oun(julgi24[yearSelect][25], 8)
                .getTimeInMillis()) + "~";
        strUngi += formatter.format(oun(julgi24[yearSelect][26], -1)
                .getTimeInMillis()) + " ";
        strUngi += ungiOneYear(oun(ungiDate[9], 0)) + "\n";
        // 5운 6기
        strUngi += formatter.format(oun(julgi24[yearSelect][26], 0)
                .getTimeInMillis()) + "~12/31 ";
        strUngi += ungiOneYear(oun(ungiDate[10], 0));
        tV = (TextView) findViewById(R.id.textYear);
        tV.setText(strUngi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_lastyear:
                year--;
                ungiSelectYear();
                break;

            case R.id.button_nextyear:
                year++;
                ungiSelectYear();
        }
    }

    // 5운 6기 결정
    public String ungiOneYear(Calendar dateSelect) {
        // TODO Auto-generated method stub
        String resultUngi = "";

        getUnOne(Integer.parseInt(julgi24[yearSelect][2]));
        if (dateSelect.getTimeInMillis() < daehan.getTimeInMillis()) {
            // 대한 이전(전년도 5운)
            getUnOne(Integer.parseInt(julgi24[yearSelect - 1][2]));
            unOne += 4;
        } else if (dateSelect.getTimeInMillis() <= oun(julgi24[yearSelect][11],
                -4).getTimeInMillis()) {
            // 청명 4일전까지 = 1운
        } else if (dateSelect.getTimeInMillis() <= oun(julgi24[yearSelect][15],
                1).getTimeInMillis()) {
            // 망종 2일후까지 = 2운
            unOne += 1;
        } else if (dateSelect.getTimeInMillis() <= oun(julgi24[yearSelect][19],
                4).getTimeInMillis()) {
            // 입추 5일후까지 = 3운
            unOne += 2;
        } else if (dateSelect.getTimeInMillis() <= oun(julgi24[yearSelect][25],
                7).getTimeInMillis()) {
            // 입동 8일후까지 = 4운
            unOne += 3;
        } else {
            // 다음 해 대한이전까지 = 5운
            unOne += 4;
        }
        // TextView tV1 = (TextView) findViewById(R.id.textLeft);
        // tV1.setText(owhal[unOne % 5] + "( )" +
        // strMoreLess[moreless]);
        resultUngi += owhal[unOne % 5];

        // 운기중에서 기
        getGiOne(Integer.parseInt(julgi24[yearSelect][4]));
        if (dateSelect.getTimeInMillis() < daehan.getTimeInMillis()) {
            // 대한 이전(전년도 5운)
            getGiOne(Integer.parseInt(julgi24[yearSelect - 1][4]));
            giOne += 5;
        } else if (dateSelect.getTimeInMillis() < oun(julgi24[yearSelect][10],
                0).getTimeInMillis()) {
            // 춘분전일까지 = 1기
        } else if (dateSelect.getTimeInMillis() < oun(julgi24[yearSelect][14],
                0).getTimeInMillis()) {
            // 소만전일까지 = 2기
            giOne += 1;
        } else if (dateSelect.getTimeInMillis() < oun(julgi24[yearSelect][18],
                0).getTimeInMillis()) {
            // 대서전일까지 = 3기
            giOne += 2;
        } else if (dateSelect.getTimeInMillis() < oun(julgi24[yearSelect][22],
                0).getTimeInMillis()) {
            // 추분전일까지 = 4기
            giOne += 3;
        } else if (dateSelect.getTimeInMillis() < oun(julgi24[yearSelect][26],
                0).getTimeInMillis()) {
            // 소설전일까지 = 5기
            giOne += 4;
        } else {
            // 다음 해 대한이전까지 = 6기
            giOne += 5;
        }
        getGiOne(giOne);
        // Toast.makeText(Ungi.this, Integer.toString(unOne),
        // Toast.LENGTH_SHORT).show();
        resultUngi += owhal[unOne] + strMoreLess[moreless];
        unOne = 0; // 대운 or 1운
        giOne = 0; // 1기
        moreless = 0; // 태과, 불급
        return resultUngi;

    }

    private String daehanSelect() {
        // TODO Auto-generated method stub
        String ganJi = null;

        for (int nRow = 0; nRow < 151; nRow++) {
            if (year == Integer.parseInt(julgi24[nRow][0])) {
                ganJi = julgi24[nRow][1] + julgi24[nRow][3];
                Log.i("selectedyear", ganJi);

                yearSelect = nRow;

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

                if (ganJi.equals("무진")
                        || ganJi.equals("무술")
                        || ganJi.equals("경인")
                        || ganJi.equals("경신")
                        || ganJi.equals("경오")
                        || ganJi.equals("경자")
                        || ganJi.equals("기축")
                        || ganJi.equals("기미")
                        || ganJi.equals("을유")
                        || ganJi.equals("신해")
                        || ganJi.equals("계사")
                        || ganJi.equals("정묘")) {
                    daehan = oun(julgi24[nRow][6], 0);
                }
                break; // for 문에서 빠져 나감.
            }
        }
        return ganJi;
    }

    public void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

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
//                    Log.i("nRowEndIndex", Integer.toString(nRowEndIndex));
//                    Log.i("nColumnEndIndex", Integer.toString(nColumnEndIndex));

                    for (int nRow = nRowStartIndex; nRow < nRowEndIndex; nRow++) {
                        for (int nCol = nColumnStartIndex; nCol < nColumnEndIndex; nCol++) {
                            julgi24[nRow][nCol] = sheet.getCell(nCol, nRow)
                                    .getContents();
/*
                            Log.i("Julgi", sheet.getCell(nCol, nRow)
                                    .getContents());
*/
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
        // Toast.makeText(Ungi.this, Integer.toString(unOne),
        // Toast.LENGTH_SHORT).show();
    }

    // 엑셀 셀에 있는 날짜를 Calendar 형식으로 바꿈
    public Calendar oun(String strDate, int i) {
        Calendar cal = Calendar.getInstance();

        String strMonth = strDate.substring(0, strDate.indexOf("-"));
        String strDay = strDate.substring(strDate.indexOf("-") + 1);
        // Toast.makeText(Ungi.this, strMonth, Toast.LENGTH_SHORT).show();
        // Toast.makeText(Ungi.this, strDay, Toast.LENGTH_SHORT).show();

        cal.set(year, Integer.parseInt(strMonth) - 1, Integer.parseInt(strDay)
                + i);

        // Toast.makeText(Ungi.this, strDate, Toast.LENGTH_SHORT).show();
        return cal;
    }

}
