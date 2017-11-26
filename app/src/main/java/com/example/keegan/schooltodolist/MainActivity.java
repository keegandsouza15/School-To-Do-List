package com.example.keegan.schooltodolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Adapters.AssignmentDisplayAdapter;
import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.Utilities.AlarmUtil;
import com.example.keegan.schooltodolist.Utilities.NotificationUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity
    implements AssignmentDisplayAdapter.AssignmentItemOnClickListener ,SharedPreferences.OnSharedPreferenceChangeListener{

    private Button mTestButton;

    private FloatingActionButton addNewTaskFab, mEditCourseButton, mEditAssignmentButton;

    private RecyclerView mRecyclerView;
    private AssignmentDisplayAdapter mAdapter;

    private TextView mEditCourses,mEditAssignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.v("Preferences", String.valueOf(sharedPreferences.getBoolean("notifications",true)));

        Log.v("Called","MainActivity-onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAssignment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AssignmentDisplayAdapter(this,this,mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);




        addNewTaskFab = (FloatingActionButton) findViewById(R.id.add_new_task_fab);

        mEditCourses = (TextView) findViewById(R.id.text_view_edit_courses);
        mEditAssignments = (TextView) findViewById(R.id.text_view_edit_assignment);


        mEditCourseButton = (FloatingActionButton) findViewById(R.id.button_edit_courses);
        mEditAssignmentButton = (FloatingActionButton) findViewById(R.id.button_edit_assignment);

        mTestButton = (Button) findViewById(R.id.button_notification_test);




        mEditCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editCourseIntent = new Intent(MainActivity.this,EditCourseActivity.class);
                startActivity(editCourseIntent);
            }
        });




        mEditAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAssignmentIntent = new Intent(MainActivity.this, EditAssignmentActivity.class);
                startActivity(editAssignmentIntent);
            }
        });

        addNewTaskFab.setOnClickListener(new View.OnClickListener() {
            boolean stateClear;

            @Override
            public void onClick(View v) {

                if (!stateClear) {

                    mEditCourseButton.setClickable(true);
                    mEditAssignmentButton.setClickable(true);

                    mEditCourses.setVisibility(View.VISIBLE);
                    mEditAssignments.setVisibility(View.VISIBLE);
                    mEditCourses.animate().yBy(-195);
                    mEditAssignments.animate().yBy(-345);

                    mEditCourses.animate().xBy(-134);
                    mEditAssignments.animate().xBy(-134);


                    mEditAssignmentButton.setVisibility(View.VISIBLE);
                    mEditCourseButton.setVisibility(View.VISIBLE);
                    mEditCourseButton.animate().yBy(-175);
                    mEditAssignmentButton.animate().yBy(-325);
                    addNewTaskFab.setImageDrawable(getDrawable(R.drawable.ic_clear_fab));
                    stateClear = true;
                }
                else {


                    mEditCourseButton.setClickable(false);
                    mEditAssignmentButton.setClickable(false);

                    final long timeCourse =  mEditCourseButton.animate().yBy(175).getDuration();
                    mEditAssignmentButton.animate().yBy(325).getDuration();

                    mEditCourses.animate().yBy(195);
                    mEditAssignments.animate().yBy(345);

                    mEditCourses.animate().xBy(134);
                    mEditAssignments.animate().xBy(134);




                    AsyncTask asyncTask = new AsyncTask() {
                       @Override
                       protected Object doInBackground(Object[] params) {
                           Long time = SystemClock.elapsedRealtime();
                           while (SystemClock.elapsedRealtime() < time + timeCourse){}
                           return null;
                       }
                       @Override
                       protected void onPostExecute(Object o) {
                           super.onPostExecute(o);
                           mEditCourseButton.setVisibility(View.INVISIBLE);
                           mEditAssignmentButton.setVisibility(View.INVISIBLE);
                           mEditCourses.setVisibility(View.INVISIBLE);
                           mEditAssignments.setVisibility(View.INVISIBLE);
                           addNewTaskFab.setImageDrawable(getDrawable(R.drawable.ic_add_fab));
                       }
                   };asyncTask.execute();
                    stateClear = false;
                }
            }
        });


        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Testing reminder Data;
                Date currentDate = Calendar.getInstance().getTime();
                currentDate = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate());
                Date dueDate = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate()+1,0,0);
                AssignmentDataBaseHelper assignmentDataBaseHelper = new AssignmentDataBaseHelper(getApplicationContext());
                SQLiteDatabase db = assignmentDataBaseHelper.getReadableDatabase();
                String a = assignmentDataBaseHelper.getDueNextDaysAssignments(db,dueDate.toString());
                String b = assignmentDataBaseHelper.getDueNextDaysAssignments(db,currentDate.toString());
                String c = a + b;
                String cc = c.substring(0,c.length()-2);
                NotificationUtils.remindUserofDueDate(getApplicationContext(),cc);
            }
        });

    }

    @Override
    protected void onResume() {
        Log.v("Called","MainActivity-onResume");
        super.onResume();
        mRecyclerView.setAdapter(new AssignmentDisplayAdapter(MainActivity.this,this,mRecyclerView));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("Called","MainActivty-onSavedInstanceState");
    }

    @Override
    public void onAssignmentClicked(int position) {
        Toast.makeText(getApplicationContext(),"This assignment is due today",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.action_settings){
            Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish ();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        boolean notifications_on = sharedPreferences.getBoolean(key,false);
        Log.v(key,String.valueOf(notifications_on));
        if (notifications_on){
            AlarmUtil alarmUtil = new AlarmUtil();
            alarmUtil.setAlarm(this);
        }
        else {
            Context context = this;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmUtil.class);
            PendingIntent sender = PendingIntent.getBroadcast(context,
                    192837,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(sender);
        }
    }
}
