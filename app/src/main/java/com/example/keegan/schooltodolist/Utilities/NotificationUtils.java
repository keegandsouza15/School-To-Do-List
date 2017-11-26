package com.example.keegan.schooltodolist.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.keegan.schooltodolist.MainActivity;
import com.example.keegan.schooltodolist.R;
import com.example.keegan.schooltodolist.sync.DueDateReminderIntentService;
import com.example.keegan.schooltodolist.sync.ReminderTasks;

/**
 * Created by Keegan on 7/14/2017.
 */

public class NotificationUtils {

    private static final int DUEDATE_REMINDER_NOTIFCATION_ID = 2932;

    private static final int DUEDATE_REMINDER_PENDING_INTENT_ID = 1323;

    public static void remindUserofDueDate (Context context,String Assignments){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(Color.GRAY)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Here are the assignments that are due  within the next day")
                .setContentText(Assignments)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
              //  .addAction(test(context))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(DUEDATE_REMINDER_NOTIFCATION_ID,notificationBuilder.build());

    }

    private static NotificationCompat.Action test (Context context) {

        Intent testIntent = new Intent(context, DueDateReminderIntentService.class);

        testIntent.setAction(ReminderTasks.ACTION_REMIND_USER_OF_DUE_DATE);

        PendingIntent testpendintIntent = PendingIntent.getService(
                context,
                DUEDATE_REMINDER_PENDING_INTENT_ID,
                testIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action(R.mipmap.ic_launcher_round,
                "test",
                testpendintIntent);
        return action;

    }

    private static PendingIntent contentIntent (Context context){
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                DUEDATE_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }



}
