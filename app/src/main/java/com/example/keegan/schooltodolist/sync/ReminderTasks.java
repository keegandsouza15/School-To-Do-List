package com.example.keegan.schooltodolist.sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Keegan on 7/14/2017.
 */

public class ReminderTasks {

    public static final String ACTION_REMIND_USER_OF_DUE_DATE = "remind_due_date";

    public static void executeTask (Context context, String action){
        Log.v("executeTask",action);

        if (ACTION_REMIND_USER_OF_DUE_DATE.equals(action)){
            Log.v("Hello","There");
        }
    }

}
