package com.example.keegan.schooltodolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Adapters.EditAssignmentDisplayAdapter;
import com.example.keegan.schooltodolist.Data.AssignmentContract;
import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;

import java.util.ArrayList;

public class EditAssignmentActivity extends AppCompatActivity implements EditAssignmentDisplayAdapter.MyClickListeners {

    RecyclerView mRecyclerView;
    EditAssignmentDisplayAdapter mAdapter;

    SQLiteDatabase mDatabase;
    AssignmentDataBaseHelper assignmentDataBaseHelper;

    Button mAddAssignmentButton;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        mContext = getApplicationContext();

        assignmentDataBaseHelper = new AssignmentDataBaseHelper(this);
        mDatabase = assignmentDataBaseHelper.getWritableDatabase();

        mAddAssignmentButton = (Button) findViewById(R.id.button_add_assignment);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_edit_assignments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DataObject dataObject = populateData();
        mAdapter = new EditAssignmentDisplayAdapter(this, this,mDatabase,assignmentDataBaseHelper,
                dataObject._mAssignmentNames,
                dataObject._mAssignmentIds,
                dataObject._mCourseNames,
                dataObject._areCompleted);

        mRecyclerView.setAdapter(mAdapter);

        mAddAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAssignmentIntent = new Intent(EditAssignmentActivity.this,EnterAssignmentActivity.class);
                addAssignmentIntent.putExtra("New Assignment", false);
                startActivity(addAssignmentIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent mainActitvity = new Intent(this,MainActivity.class);
        startActivity(mainActitvity);
    }


    public DataObject populateData () {
        ArrayList<String> mAssignmentNames = new ArrayList<>();
        ArrayList<Integer> mAssignmentIds = new ArrayList<>();
        ArrayList<Integer> areCompleted = new ArrayList<>();
        ArrayList<Integer> courseIds = new ArrayList<>();
        Cursor cursor = assignmentDataBaseHelper.getAllAssignmentsFromDataBase(mDatabase, true);
        while (cursor.moveToNext()) {
            mAssignmentNames.add(cursor.getString(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_ASSIGNMENT_NAME)));
            mAssignmentIds.add(cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable._ID)));
            courseIds.add(cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_COURSE_ID)));
            areCompleted.add(cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_FINISHED)));
        }
        ArrayList<String> mCourseNames = new ArrayList<>();
        // Finds the course names;
        String courseNameFromAssignmentId;
        for (int i = 0; i < mAssignmentNames.size(); i++) {
            if (courseIds.get(i) != -1) {
                courseNameFromAssignmentId = assignmentDataBaseHelper.getCourseNameFromAssignmentId(mDatabase, courseIds.get(i));
            } else {
                courseNameFromAssignmentId = "";
            }
            mCourseNames.add(courseNameFromAssignmentId);
        }
        DataObject dataObject = new DataObject();
        dataObject._mAssignmentNames = mAssignmentNames;
        dataObject._mAssignmentIds = mAssignmentIds;
        dataObject._mCourseNames = mCourseNames;
        dataObject._areCompleted = areCompleted;
        return dataObject;
    }

    @Override
    public void onReinstateButtonClicked(View v, int assignmentId) {
        Button mReinstateButton = (Button) v.findViewById(R.id.assignment_item_edit_reinstate_button);
        assignmentDataBaseHelper.updateAssignmentFinished(mDatabase,assignmentId,0);
        assignmentDataBaseHelper.updateTimeCompleted(mDatabase,assignmentId,0);
        mReinstateButton.setVisibility(View.INVISIBLE);
        Toast.makeText(mContext,"The Assignment has been reinstated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditButtonClicked(int assignmentId) {
        Intent startEnterAssignment = new Intent(EditAssignmentActivity.this,EnterAssignmentActivity.class);
        startEnterAssignment.putExtra("New Assignment",true);
        startEnterAssignment.putExtra("Assignment Id", assignmentId);
        startActivity(startEnterAssignment);
    }

    @Override
    public void onDeleteButtonClicked(int assignmentId, int position) {
        assignmentDataBaseHelper.deleteAssignmentsFromDataBase(mDatabase,assignmentId);
        mAdapter.mAssignmentNames.remove(position);
        mAdapter.mAssignmentIds.remove(position);
        mAdapter.mCourseNames.remove(position);
        mAdapter.areCompleted.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.mNumberOfItems--;
    }

    class DataObject {
        public  ArrayList<String> _mAssignmentNames;
        public  ArrayList<Integer> _mAssignmentIds;
        public ArrayList<Integer> _areCompleted;
        public ArrayList<String> _mCourseNames;
    }


}
