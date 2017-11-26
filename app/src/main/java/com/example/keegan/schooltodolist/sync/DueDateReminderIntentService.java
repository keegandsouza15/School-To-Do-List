package com.example.keegan.schooltodolist.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Keegan on 7/14/2017.
 */

public class DueDateReminderIntentService extends IntentService {

    public DueDateReminderIntentService(){
        super("WaterReminderIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("ONhandleIntent","Called");
        String action = intent.getAction();
        ReminderTasks.executeTask(this,action);
    }
}
