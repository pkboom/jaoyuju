package com.sujichim.jasanjao2;

import android.util.Log;

/**
 * Created by yedam on 15. 8. 27.
 */
public class UngiJangbu {
    private String jang;
    private String bu;
    private String[] jangStatus = new String[5];
    private String[] buStatus = new String[5];

    public UngiJangbu(String temp) {
        jang = temp.substring(0, 2);
        bu = temp.substring(temp.indexOf(",")+2, temp.length());
        Log.i("temp", temp + Integer.toString(temp.length()));
        Log.i("jang", jang + Integer.toString(jang.length()));
        Log.i("bu", bu + Integer.toString(bu.length()));

        SungHu();
    }

    //각 장부의 승허 파악
    public void SungHu() {
        String[] oJang = {"간승", "심승", "비승", "폐승", "신승"};
        String[] oBu = {"담승", "소장승", "위승", "대장승", "방광승"};
        int numJang = 0;
        int numBu = 0;

        Log.i("jang", jang.substring(0,1));
        switch (jang.substring(0,1)) {
            case "간":
                numJang = 0;
                break;
            case "심":
                numJang = 1;
                break;
            case "비":
                numJang = 2;
                break;
            case "폐":
                numJang = 3;
                break;
            case "신":
                numJang = 4;
                break;
        }

        Log.i("bu", bu.substring(0,1));
        switch (bu.substring(0,1)) {
            case "담":
                numBu = 0;
                break;
            case "소":
                numBu = 1;
                break;
            case "위":
                numBu = 2;
                break;
            case "대":
                numBu = 3;
                break;
            case "방":
                numBu = 4;
                break;
        }

        Log.i("sunghu_jang", jang.substring(1));
        if (jang.substring(1).equals("승")) {
            jangStatus[0] = oJang[(numJang+4)%5] + " \uD83D\uDE1F"; //worried
            jangStatus[1] = oJang[numJang] + " \uD83D\uDE1F"; //worried
            jangStatus[2] = oJang[(numJang+1)%5] + " \uD83D\uDE1F"; //worried
            jangStatus[3] = oJang[(numJang+2)%5] + " \uD83D\uDE0A"; //smile
            jangStatus[4] = oJang[(numJang+3)%5] + " \uD83D\uDE0A"; //smile
        } else { //허
            jangStatus[0] = oJang[(numJang+4)%5] + " \uD83D\uDE0A"; //smile
            jangStatus[1] = oJang[numJang] + " \uD83D\uDE0A"; //smile
            jangStatus[2] = oJang[(numJang+1)%5] + " \uD83D\uDE0A"; //smile
            jangStatus[3] = oJang[(numJang+2)%5] + " \uD83D\uDE1F"; //worried
            jangStatus[4] = oJang[(numJang+3)%5] + " \uD83D\uDE1F"; //worried
        }

        Log.i("sunghu_bu", bu.substring(bu.length()-1));
        if (bu.substring(bu.length()-1).equals("승")) {
            buStatus[0] = oBu[(numBu+4)%5] + " \uD83D\uDE1F"; //worried
            buStatus[1] = oBu[numBu] + " \uD83D\uDE1F"; //worried
            buStatus[2] = oBu[(numBu+1)%5] + " \uD83D\uDE1F"; //worried
            buStatus[3] = oBu[(numBu+2)%5] + " \uD83D\uDE0A"; //smile
            buStatus[4] = oBu[(numBu+3)%5] + " \uD83D\uDE0A"; //smile
        } else { //허
            buStatus[0] = oBu[(numBu+4)%5] + " \uD83D\uDE0A"; //smile
            buStatus[1] = oBu[numBu] + " \uD83D\uDE0A"; //smile
            buStatus[2] = oBu[(numBu+1)%5] + " \uD83D\uDE0A"; //smile
            buStatus[3] = oBu[(numBu+2)%5] + " \uD83D\uDE1F"; //worried
            buStatus[4] = oBu[(numBu+3)%5] + " \uD83D\uDE1F"; //worried
        }
    }

    public String[] getJangStatus() {
        return jangStatus;
    }

    public String[] getBuStatus() {
        return buStatus;
    }
}
