package com.example.keegan.schooltodolist.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.MainActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Keegan on 7/20/2017.
 */

public class AlarmUtil extends BroadcastReceiver{



    public void setAlarm (Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set (Calendar.SECOND,0);
        Log.v("Alarm set for", calendar.getTime().toString());
        Intent intent = new Intent(context, AlarmUtil.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,
                192837,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,sender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Called","daf");
        Date currentDate = Calendar.getInstance().getTime();
        currentDate = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate());
        Date dueDate = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate()+1,0,0);
        AssignmentDataBaseHelper assignmentDataBaseHelper = new AssignmentDataBaseHelper(context);
        SQLiteDatabase db = assignmentDataBaseHelper.getReadableDatabase();
        String a = assignmentDataBaseHelper.getDueNextDaysAssignments(db,dueDate.toString());
        String b = assignmentDataBaseHelper.getDueNextDaysAssignments(db,currentDate.toString());
        if (a.length() == 0&& (b.length() == 0))
            return;
        String c = a + b;
        String cc = c.substring(0,c.length()-2);
        if (!(cc.length() == 0))
            NotificationUtils.remindUserofDueDate(context,cc);
    }
}