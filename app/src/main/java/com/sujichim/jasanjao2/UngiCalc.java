package com.sujichim.jasanjao2;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.Calendar;

import jxl.Sheet;
import jxl.Workbook;

public class UngiCalc {
    private String[][] julgi24 = new String[151][29];
    private String[] owhal = {"목", "화", "토", "금", "수"};
    private String[] jang = {"간", "심", "비", "폐", "신"};
    private String[] strMoreLess = {"태과", "불급"};
    private String resultUngi = null;
    private String resultUngiBoth = null;
    private String resultJangBu;

    int year, month, day;
    int unOne; // 대운 or 1운
    int giOne; // 1기
    int moreless; // 태과, 불급
    boolean leftRight; // default = left


    private Context context;

    public UngiCalc(Context context, int year2, int month2, int day2, Boolean leftRight) {
        this.context = context;
        unOne = 0; // 대운 or 1운
        giOne = 0; // 1기
        moreless = 0; // 태과, 불급
        this.leftRight = leftRight; // default = left

        copyExcelDataToDatabase();

/*
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
*/

        year = year2;
        month = month2;
        day = day2;
//        Log.i("year/month/day", Integer.toString(year) + Integer.toString(month) + Integer.toString(day));
        // 우측 운기체형을 하면 선택된 날짜가 변경이 되어 미리 저장해둠.
        // 스마트폰 화면에 나타날 년 월 일 저장.

        resultUngi = year + "년 " + (month + 1) + "월 " + day + "일" + "\n";
//        Log.i("resultUngi", resultUngi);

        leftUngi();
    }

    public String getUngiLeft() {
        return resultUngi;
    }

    public String getUngiBoth() {
        return resultUngiBoth;
    }

    public String getJangbu() {
        return resultJangBu.substring(1, resultJangBu.length()-1);
    }

    // 운기중에서 운
    public void leftUngi() {
        // TODO Auto-generated method stub
        Calendar cal = Calendar.getInstance();
        Calendar daehan = Calendar.getInstance();
        int samhap;
        String[] bu = {"담", "소장", "위", "대장", "방광"};
        String[] strMoreLess2 = {"승", "허"};
        String resultJisu = null;
        String resultOwhal = null;
        String fiveSix = null;
        String temp = null;

        for (int nRow = 0; nRow < 151; nRow++) {
            if (year == Integer.parseInt(julgi24[nRow][0])) {
                temp = julgi24[nRow][1] + julgi24[nRow][3];
//                Log.i("selectedyear", temp);
                // 대운과 태급,불과 결정
                getUnOne(Integer.parseInt(julgi24[nRow][2]));

                // 선택된 날짜
                cal.set(year, month, day);
//                Log.i("year/month/day", Integer.toString(year) + Integer.toString(month) + Integer.toString(day));

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

                if (cal.getTimeInMillis() < daehan.getTimeInMillis()) {
                    // 대한 이전(전년도 5운)
                    getUnOne(Integer.parseInt(julgi24[nRow - 1][2]));
                    unOne += 4;
                    fiveSix = "5운";
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][11], -4)
                        .getTimeInMillis()) {
                    // 청명 4일전까지 = 1운
                    fiveSix = "1운";
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][15], 1)
                        .getTimeInMillis()) {
                    // 망종 2일후까지 = 2운
                    unOne += 1;
                    fiveSix = "2운";
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][19], 4)
                        .getTimeInMillis()) {
                    // 입추 5일후까지 = 3운
                    unOne += 2;
                    fiveSix = "3운";
                } else if (cal.getTimeInMillis() <= oun(julgi24[nRow][25], 7)
                        .getTimeInMillis()) {
                    // 입동 8일후까지 = 4운
                    unOne += 3;
                    fiveSix = "4운";
                } else {
                    // 다음 해 대한이전까지 = 5운
                    unOne += 4;
                    fiveSix = "5운";
                }


                resultOwhal = owhal[unOne % 5];
                resultJangBu = "(" + jang[unOne % 5] + strMoreLess2[moreless];
                samhap = unOne % 5;

                resultUngiBoth = " " + owhal[unOne % 5];
                resultJisu = "("
                        + jang[unOne % 5]
                        + " "
                        + (gujoJisu(jang[unOne % 5])
                        + ungiJisu(jang[unOne % 5], strMoreLess2[moreless]));
//                Log.i("unOne", String.valueOf(unOne));
//                Log.i("moreless", String.valueOf(moreless));
//                Log.i("jang[unOne % 5]", jang[unOne % 5]);
//                Log.i("gujoJisu", String.valueOf(gujoJisu(jang[unOne % 5])));
//                Log.i("ungiJisu", String.valueOf(ungiJisu(jang[unOne % 5], strMoreLess2[moreless])));
//                Log.i("samhap_first", resultJisu);

                // 운기중에서 기
                getGiOne(Integer.parseInt(julgi24[nRow][4]));
                if (cal.getTimeInMillis() < daehan.getTimeInMillis()) {
                    // 대한 이전(전년도 5운)
                    getGiOne(Integer.parseInt(julgi24[nRow - 1][4]));
                    giOne += 5;
                    fiveSix += "6기";
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][10], -1)
                        .getTimeInMillis()) {
                    // 춘분전일까지 = 1기
                    fiveSix += "1기";
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][14], -1)
                        .getTimeInMillis()) {
                    // 소만전일까지 = 2기
                    giOne += 1;
                    fiveSix += "2기";
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][18], -1)
                        .getTimeInMillis()) {
                    // 대서전일까지 = 3기
                    giOne += 2;
                    fiveSix += "3기";
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][22], -1)
                        .getTimeInMillis()) {
                    // 추분전일까지 = 4기
                    giOne += 3;
                    fiveSix += "4기";
                } else if (cal.getTimeInMillis() < oun(julgi24[nRow][26], -1)
                        .getTimeInMillis()) {
                    // 소설전일까지 = 5기
                    giOne += 4;
                    fiveSix += "5기";
                } else {
                    // 다음 해 대한이전까지 = 6기
                    giOne += 5;
                    fiveSix += "6기";
                }
                // Toast.makeText(Ungi.this, Integer.toString(giOne),
                // Toast.LENGTH_SHORT).show();

                getGiOne(giOne);
                resultUngi += fiveSix + ": ";
                resultOwhal += owhal[unOne] + strMoreLess[moreless] + "\n";
                resultUngi += resultOwhal;

                resultUngiBoth += owhal[unOne] + strMoreLess[moreless] + "\n";

                // 삼합이면 장부가 서로 반대
                if (samhap == unOne || samhap == (unOne + 4) % 5
                        || samhap == (unOne + 6) % 5) {
                    resultJangBu += ", " + bu[unOne]
                            + strMoreLess2[(moreless + 1) % 2] + ")";
                    resultUngi += resultJangBu;

                    resultUngiBoth += resultJangBu + " \n";
                    resultJisu += ", "
                            + bu[unOne]
                            + " "
                            + (gujoJisu(bu[unOne]) + ungiJisu(bu[unOne],
                            strMoreLess2[(moreless + 1) % 2])) + ") \n";
                    resultUngiBoth += resultJisu;
//                    Log.i("samhap", resultJisu);
                } else { //상극 상외이면 장부는 승허가 서로 같음
                    resultJangBu += ", " + bu[unOne]
                            + strMoreLess2[moreless] + ")";
                    resultUngi += resultJangBu;

                    resultUngiBoth += resultJangBu + " \n";
                    resultJisu += ", "
                            + bu[unOne]
                            + " "
                            + (gujoJisu(bu[unOne]) + ungiJisu(bu[unOne],
                            strMoreLess2[moreless])) + ") \n";
                    resultUngiBoth += resultJisu;
//                    Log.i("sangkk", resultJisu);
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
        //i만큼 날짜 이동
        Calendar cal = Calendar.getInstance();

        String strMonth = strDate.substring(0, strDate.indexOf("-"));
        String strDay = strDate.substring(strDate.indexOf("-") + 1);
        cal.set(year, Integer.parseInt(strMonth) - 1, Integer.parseInt(strDay)
                + i);

        return cal;
    }

    public void copyExcelDataToDatabase() {
        //Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet;

        try {
            InputStream is = context.getResources().getAssets().open("julgi24.xls");

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
}
