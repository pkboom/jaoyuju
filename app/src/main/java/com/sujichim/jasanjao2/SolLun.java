package com.sujichim.jasanjao2;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by kb on 17. 1. 24.
 */

public class SolLun {
        private String[] lunarTable = {
                "1212122322121", // 1881
                "1212121221220",
                "1121121222120",
                "2112132122122",
                "2112112121220",
                "2121211212120",
                "2212321121212",
                "2122121121210",
                "2122121212120",
                "1232122121212",
                "1212121221220", // 1891
                "1121123221222",
                "1121121212220",
                "1212112121220",
                "2121231212121",
                "2221211212120",
                "1221212121210",
                "2123221212121",
                "2121212212120",
                "1211212232212",
                "1211212122210", // 1901
                "2121121212220",
                "1212132112212",
                "2212112112210",
                "2212211212120",
                "1221412121212",
                "1212122121210",
                "2112212122120",
                "1231212122212",
                "1211212122210",
                "2121123122122", // 1911
                "2121121122120",
                "2212112112120",
                "2212231212112",
                "2122121212120",
                "1212122121210",
                "2132122122121",
                "2112121222120",
                "1211212322122",
                "1211211221220",
                "2121121121220", // 1921
                "2122132112122",
                "1221212121120",
                "2121221212110",
                "2122321221212",
                "1121212212210",
                "2112121221220",
                "1231211221222",
                "1211211212220",
                "1221123121221",
                "2221121121210", // 1931
                "2221212112120",
                "1221241212112",
                "1212212212120",
                "1121212212210",
                "2114121212221",
                "2112112122210",
                "2211211412212",
                "2211211212120",
                "2212121121210",
                "2212214112121", // 1941
                "2122122121120",
                "1212122122120",
                "1121412122122",
                "1121121222120",
                "2112112122120",
                "2231211212122",
                "2121211212120",
                "2212121321212",
                "2122121121210",
                "2122121212120", //1951
                "1212142121212",
                "1211221221220",
                "1121121221220",
                "2114112121222",
                "1212112121220",
                "2121211232122",
                "1221211212120",
                "1221212121210",
                "2121223212121",
                "2121212212120", // 1961
                "1211212212210",
                "2121321212221",
                "2121121212220",
                "1212112112210",
                "2223211211221",
                "2212211212120",
                "1221212321212",
                "1212122121210",
                "2112212122120",
                "1211232122212", // 1971
                "1211212122210",
                "2121121122210",
                "2212312112212",
                "2212112112120",
                "2212121232112",
                "2122121212110",
                "2212122121210",
                "2112124122121",
                "2112121221220",
                "1211211221220", // 1981
                "2121321122122",
                "2121121121220",
                "2122112112322",
                "1221212112120",
                "1221221212110",
                "2122123221212",
                "1121212212210",
                "2112121221220",
                "1211231212222",
                "1211211212220", // 1991
                "1221121121220",
                "1223212112121",
                "2221212112120",
                "1221221232112",
                "1212212122120",
                "1121212212210",
                "2112132212221",
                "2112112122210",
                "2211211212210",
                "2221321121212", //2001
                "2212121121210",
                "2212212112120",
                "1232212122112",
                "1212122122120",
                "1121212322122",
                "1121121222120",
                "2112112122120",
                "2211231212122",
                "2121211212120",
                "2122121121210", // 2011
                "2124212112121",
                "2122121212120",
                "1212121223212",
                "1211212221220",
                "1121121221220",
                "2112132121222", //2017
                "1212112121220",
                "2121211212120",
                "2122321121212",
                "1221212121210", // 2021
                "2121221212120",
                "1232121221212",
                "1211212212210",
                "2121123212221",
                "2121121212220",
                "1212112112220",
                "1221231211221",
                "2212211211220",
                "1212212121210",
                "2123212212121", // 2031
                "2112122122120",
                "1211212322212",
                "1211212122210",
                "2121121122120",
                "2212114112122",
                "2212112112120",
                "2212121211210",
                "2212232121211",
                "2122122121210",
                "2112122122120", // 2041
                "1231212122212",
                "1211211221220",
                "2121121321222",
                "2121121121220",
                "2122112112120",
                "2122141211212",
                "1221221212110",
                "2121221221210",
                "2114121221221"
        };



        private int day_array[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


        public String SolToLun(int SolYear, int SolMonth, int SolDay) {
            int i;
            int j;
            int acc_day;
            String str;


            if((SolYear < 1881) || (SolYear > 2050)) {
                Log.i("error ", "year");
                return "error";
            }

            if(SolMonth > 11) {
                Log.i("error ", "month");
                return "error";
            }

            if((SolDay < 1) || (SolDay > 31)) {
                Log.i("error ", "day");
                return "error";
            }

            int yearIndex = SolYear - 1881;

            int[] iDayCount = new int[170];

            for (i = 0; i < 170; i++){
                iDayCount[i] = 0;

                for (j = 0; j < lunarTable[i].length(); j++){
                    switch (Integer.parseInt(lunarTable[i].substring(j, j + 1))) {
                        case 1:
                        case 3:
                            iDayCount[i] += 29;
                            break;
                        case 2:
                        case 4:
                            iDayCount[i] += 30;
                            break;
                    }
                }
            }

            //total days

            SolYear--;

            int total_day = SolYear * 365 + SolYear / 4 - SolYear / 100 + SolYear / 400;

            SolYear++;

            if(((SolYear % 4) == 0) && ((SolYear % 100) != 0) || ((SolYear % 400) == 0)) {
                day_array[1] = 29;
            } else {
                day_array[1] = 28;
            }

            for(i = 0; i < SolMonth ; i++) {
                total_day = total_day + day_array[i];
            }
            total_day = total_day + SolDay;
            //total days until 1880

//            acc_day = total_day - 686686 + 1;
            acc_day = total_day - (1880*365 + 1880/4 -1880/100 + 1880/400 + 30) + 1;

            //Get Lunar Year

            int buff_day = iDayCount[0];
            for(i = 0; i < lunarTable.length - 1 ; i++) {

                if(acc_day <= buff_day) break;

                buff_day = buff_day + iDayCount[i+1];

            }

            int lunYear = i + 1881;

            //Get Lunar Month

            buff_day = buff_day - iDayCount[i];

            acc_day  = acc_day - buff_day;


            int temp;
            if(lunarTable[i].substring(lunarTable[i].length()-1).equals("0")) {
                temp = 12;
            } else {
                temp = 13;
            }

            boolean isLeap = false;

//            if ("0".equals(lunarTable[yearIndex].substring(lunarTable[yearIndex].length()-1))) {
//                isLeap = false;
//            }
//
            int m2 = 0;

            for(j = 0; j < temp -1; j++) {

                int m1;
                if(Integer.parseInt(lunarTable[i].substring(j,j+1)) <= 2) {

                    isLeap = false;
                    m2++;

                    m1 = Integer.parseInt(lunarTable[i].substring(j,j+1)) + 28;

                } else {
                    isLeap = true;
                    m1 = Integer.parseInt(lunarTable[i].substring(j,j+1)) + 26;
                }

                if(acc_day <= m1) break;
                acc_day = acc_day - m1;
            }

            int lunMonth = m2;
            Log.i("isLeap ", String.valueOf(isLeap));

            //Get Lunar Day

            int lunDay = acc_day;

            //return date string
            str = Integer.toString(lunYear);

            if(lunMonth < 10) str += "0"+ lunMonth;

            else str += lunMonth;



            if(lunDay < 10) str += "0"+ lunDay;

            else str += lunDay;

            if(isLeap) str += "(윤달)";



            return str;
        }


        public String LunToSol(int LunYear, int LunMonth, int LunDay, int isLeap) {
            int i;
            int j;
            int acc_day;
            String str;

            if((LunYear < 1881) || (LunYear > 2050))  {
                Log.i("error ", "year");
                return "error";
            }

            if(LunMonth > 11) {
                Log.i("error ", "month");
                return "error";
            }

            if((LunDay < 1) || (LunDay > 30)) {
                Log.i("error ", "Day");
                return "error";
            }


            int yy = -1;

            acc_day = 0;

            if(LunYear != 1881) {

                yy = LunYear - 1882;

                for(i = 0; i <= yy; i++) {

                    for(j = 0; j <= 12; j++) {
                        acc_day = acc_day + Integer.parseInt(lunarTable[i].substring(j,j+1));
                    }

                    if(lunarTable[i].substring(12,13).equals("0")) {
                        acc_day = acc_day + 336;
                    } else {
                        acc_day = acc_day + 362;
                    }

                }

            }

            yy++;
            int n2 = LunMonth;
            int mm = -1;

            while(true) {
                mm++;
                if(Integer.parseInt(lunarTable[yy].substring(mm, mm +1)) > 2) {
                    acc_day = acc_day + 26 + Integer.parseInt(lunarTable[yy].substring(mm, mm +1));
                    n2++; //if there is lunar month, add one more month
                } else {
                    if(mm == n2) {
                        break;
                    } else {
                        acc_day = acc_day + 28 + Integer.parseInt(lunarTable[yy].substring(mm, mm +1));
                    }
                }
            }

            // Leap Year
            if(isLeap > 0) {
                Log.i("LunMonth  ", String.valueOf(LunMonth));
                Log.i("LunMonth Leap?  ", lunarTable[yy].substring(LunMonth+1, LunMonth + 2));
                if(Integer.parseInt(lunarTable[yy].substring(LunMonth+1, LunMonth + 2)) > 2) {
                    Log.i("yy  ", String.valueOf(yy));
                    acc_day = acc_day + 28 + Integer.parseInt(lunarTable[yy].substring(mm, mm +1));

                } else {
                    Log.i("yy  ", String.valueOf(yy));
                    Log.i("Leap year is wrong  ", String.valueOf(isLeap));
                    return "error";
                }
            }

            acc_day = acc_day + LunDay + 29;

            yy = 1880;

            while(true) {

                yy++;

                mm = 365;

                if((yy % 4) == 0 && ((yy % 100) != 0 || (yy % 400) == 0)) mm = 366;

                if(acc_day <= mm) break;

                acc_day = acc_day - mm;

            }

            int solYear = yy;

            day_array[1] = mm - 337; //29 or 28 for febuary

            yy = 0;

            while(true) {

                yy++;

                if(acc_day <= day_array[yy -1]) break;

                acc_day = acc_day - day_array[yy -1];

            }


            int solMonth = yy;

            int solDay = acc_day;



            //return date string

            str = Integer.toString(solYear);



            if(solMonth < 10) str += "0" + Integer.toString(solMonth);

            else str += Integer.toString(solMonth);



            if(solDay < 10) str += "0" + Integer.toString(solDay);

            else str += Integer.toString(solDay);



            return str;

        }

    }

