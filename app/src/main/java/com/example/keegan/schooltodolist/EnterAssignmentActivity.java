package com.example.keegan.schooltodolist;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Data.AssignmentContract;
import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.Pickers.CoursePickerFragment;
import com.example.keegan.schooltodolist.Pickers.DatePickerFragment;
import com.example.keegan.schooltodolist.Pickers.TimePickerFragment;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EnterAssignmentActivity extends AppCompatActivity {

    SQLiteDatabase mDataBase;
    AssignmentDataBaseHelper assignmentDataBaseHelper;

    EditText AssignmentNameEditText;

    Button DudeDateButton;
    Button EstimatedTimeButton;
    Button ChooseCourseButton;

    DatePickerFragment datePickerFragment;
    TimePickerFragment timePickerFragment;
    CoursePickerFragment coursePickerFragment;



    Button enterAssignmentButton;

    Boolean isUpdating;
    int updatingAssignmentId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        Log.v("Called", "EnterAssignmentActivity");
        Intent previousIntent = getIntent();
        isUpdating = previousIntent.getBooleanExtra("New Assignment", false);
        if (isUpdating){
            updatingAssignmentId = previousIntent.getIntExtra("Assignment Id", 0);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_assignment);

        // Creates the Database helper class, and get a version of the writable database.
        assignmentDataBaseHelper = new AssignmentDataBaseHelper(this);
        mDataBase = assignmentDataBaseHelper.getWritableDatabase();


        AssignmentNameEditText = (EditText) findViewById(R.id.edit_text_assignment_name);

        DudeDateButton = (Button) findViewById(R.id.button_choose_due_date);
        EstimatedTimeButton = (Button) findViewById(R.id.button_choose_estimated_time);
        ChooseCourseButton = (Button) findViewById(R.id.button_choose_course);

        datePickerFragment = new DatePickerFragment();
        timePickerFragment = new TimePickerFragment();

        DudeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        EstimatedTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }

        });


        /*Course Name Chooser Fragment*/
        final FragmentManager fragmentManager = getFragmentManager();
        coursePickerFragment = new CoursePickerFragment();


        Cursor cursor = assignmentDataBaseHelper.getAllCourseFromDataBase(mDataBase);
        ArrayList<String> vakyes = new ArrayList<>();

        while (cursor.moveToNext()) {
            vakyes.add(cursor.getString(cursor.getColumnIndex(AssignmentContract.CourseTable.COLUMN_COURSE_NAME)));
        }

        Bundle courseNameArgs = new Bundle();
        courseNameArgs.putStringArrayList("Array_List", vakyes);
        coursePickerFragment.setArguments(courseNameArgs);

        ChooseCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursePickerFragment.show(fragmentManager, "coursePicker");
            }
        });

        enterAssignmentButton = (Button) findViewById(R.id.button_assignment_enter);
        enterAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isUpdating){
                    // Gets the assignment name
                    String assignment_name = AssignmentNameEditText.getText().toString();
                    if (assignment_name.length() == 0) {
                        AssignmentNameEditText.setError("Cannot be empty");
                        Toast.makeText(getApplicationContext(), "Please Enter a Course Name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AssignmentNameEditText.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                    AssignmentNameEditText.setTextColor(Color.WHITE);

                    // Gets the due date
                    Date due_date = datePickerFragment.getDate();
                    if (due_date == null) {
                        DudeDateButton.setError("Cannot be empty");
                        Toast.makeText(getApplicationContext(), "Please Enter a Due_Date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Gets the estimated_time
                    Time estimated_time = timePickerFragment.getTime();
                    if (estimated_time == null) {
                        EstimatedTimeButton.setError("Cannot be empty");
                        Toast.makeText(getApplicationContext(), "Please Enter am estimated time", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Other data
                    int time_completed = 0;
                    int parts = 1;
                    int parts_completed = 0;
                    int completed = 0;

                    // Gets the course name
                    String course_name = coursePickerFragment.getmChoosenCourseName();
                    if (course_name == null) {
                        ChooseCourseButton.setError("Cannot be empty");
                        Toast.makeText(getApplicationContext(), "Please Enter a course name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int course_id = assignmentDataBaseHelper.getCourseIdFromDataBase(mDataBase, course_name);


                    int hour = estimated_time.getHours() * 60;
                    int mintues = estimated_time.getMinutes();
                    Log.v("time",String.valueOf(hour + mintues));


                    long test = assignmentDataBaseHelper.insertAssignmentIntoDataBase(mDataBase,
                            assignment_name,
                            due_date.toString(),
                            hour + mintues,
                            time_completed,
                            parts,
                            parts_completed,
                            completed, course_id);

                    if (test == -1){
                        AssignmentNameEditText.setError("Same Assignment Name");
                        Toast.makeText(getApplicationContext(), "Please Enter a Different Assignment Name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(EnterAssignmentActivity.this, "Data Entered", Toast.LENGTH_SHORT).show();
                    Intent mainActivtyIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainActivtyIntent);
                }
                else {
                    if (!AssignmentNameEditText.getText().toString().equals(getOrginalassignmentName())){
                        assignmentDataBaseHelper.updateAssignmentName(mDataBase, updatingAssignmentId, AssignmentNameEditText.getText().toString());
                    }
                    if (datePickerFragment.getDate() != null){
                        if (!datePickerFragment.getDate().toString().equals(getOrginaldue_date())){
                            assignmentDataBaseHelper.updateDueDate(mDataBase, updatingAssignmentId, datePickerFragment.getDate().toString());
                        }
                    }
                    Time estimated_time = timePickerFragment.getTime();
                    if (estimated_time != null) {
                        int hour = estimated_time.getHours() * 60;
                        int mintues = estimated_time.getMinutes();
                        if ((hour + mintues) != getOrginalEsitmated_Time()) {
                            assignmentDataBaseHelper.updateEstimatedTime(mDataBase, updatingAssignmentId, hour + mintues);
                        }
                    }
                    if (coursePickerFragment.getmChoosenCourseName() != null){
                        int courseId =assignmentDataBaseHelper.getCourseIdFromDataBase(mDataBase,coursePickerFragment.getmChoosenCourseName());
                        if (courseId != getOrginalCourseId()){
                            assignmentDataBaseHelper.updateCourseId(mDataBase,updatingAssignmentId,courseId);
                        }
                    }




                    Toast.makeText(EnterAssignmentActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    Intent mainActivtyIntent = new Intent(getApplicationContext(),EditAssignmentActivity.class);
                    startActivity(mainActivtyIntent);


                }

            }
        });

        if (isUpdating){
            setOriginalData();
        }
    }

    public void setOriginalData (){
        AssignmentNameEditText.setText(getOrginalassignmentName());
        DudeDateButton.setText(getOrginaldue_date());

        int estimated_time = getOrginalEsitmated_Time();
        Time time = new Time(estimated_time/60, estimated_time % 60,0);
        EstimatedTimeButton.setText(time.toString());

        String courseName = assignmentDataBaseHelper.getCourseNameFromAssignmentId(mDataBase,getOrginalCourseId());
        ChooseCourseButton.setText(courseName);

    }

    private String getOrginalassignmentName (){
        return assignmentDataBaseHelper.getAssignmentName(mDataBase,updatingAssignmentId);
    }

    private String getOrginaldue_date (){
        return assignmentDataBaseHelper.getDueDate(mDataBase,updatingAssignmentId);
    }
    private int getOrginalEsitmated_Time (){
        return assignmentDataBaseHelper.getEsitmatedTime(mDataBase,updatingAssignmentId);
    }

    private int getOrginalCourseId (){
        return assignmentDataBaseHelper.getCourseId(mDataBase,updatingAssignmentId);

    }


}
