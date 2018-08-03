/*알람이 2시간마다 울리고
 * notification이 notifity 된다.
 */

package com.sujichim.jasanjao2;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //Toast.makeText(context, "hi from receiver", Toast.LENGTH_SHORT).show();

        //현재 app이 foreground 가 아니면 notification 을 보낸다.
        if (!isAppInForeground(context)) {
            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent notiIntent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Build notification
            // Actions are just fake
            Notification noti = new Notification.Builder(context)
                    .setContentTitle("자오유주혈자리가 바뀌었습니다.")
                    .setContentText("자산자오유주")
                    .setSmallIcon(R.drawable.hemark)
                    .setTicker("자오유주혈자리가 바뀌었습니다.")
                    .setAutoCancel(true)
                    .setContentIntent(pIntent)
                    .setSound(soundUri)
                    .build();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("MMddhh");
            String strToday = formatter.format(cal.getTime());

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            //notificationManager.notify(1369, noti);
            notificationManager.notify(Integer.parseInt(strToday), noti);
            Log.i("intNoti", strToday);
        }
    }

    //---helper method to determine if the app is in the
    // foreground---
    public static boolean isAppInForeground(Context context) {

        List<ActivityManager.RunningTaskInfo> task =((ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningTasks(1);
        if (task.isEmpty()) {
            return false;
        }
        return task.get(0).topActivity.getPackageName()
                .equalsIgnoreCase(context.getPackageName());
    }
}
