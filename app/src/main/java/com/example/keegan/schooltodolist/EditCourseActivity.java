package com.example.keegan.schooltodolist;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Adapters.EditCourseDisplayAdapter;
import com.example.keegan.schooltodolist.Data.AssignmentContract;
import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.Pickers.NewCourseDialog;

import java.util.ArrayList;

public class EditCourseActivity extends AppCompatActivity implements  EditCourseDisplayAdapter.CourseEditOnClickListener, NewCourseDialog.updateDataNewCourseDialog{

    private RecyclerView mRecyclerView;
    private EditCourseDisplayAdapter mAdapter;

    private SQLiteDatabase mDataBase;
    private AssignmentDataBaseHelper assignmentDataBaseHelper;


    private Button mAddCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        assignmentDataBaseHelper = new AssignmentDataBaseHelper(this);
        mDataBase = assignmentDataBaseHelper.getWritableDatabase();

        // Add Course Button
        mAddCourseButton = (Button) findViewById(R.id.button_add_course);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewCourseDialog newCourseDialog = new NewCourseDialog();
                newCourseDialog.setDataBase(mDataBase,assignmentDataBaseHelper, EditCourseActivity.this);
                newCourseDialog.show(getFragmentManager(),"newCourseDialog");
            }
        });

        // Gets the data from the database
        ArrayList<ArrayList<String>> databaseData;
        databaseData = getData();

        // Sets up the Recycler View and Adapter
        mAdapter = new EditCourseDisplayAdapter(this,this, databaseData.get(0), databaseData.get(1));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_edit_courses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public ArrayList<ArrayList<String>> getData (){
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Cursor cursor = assignmentDataBaseHelper.getAllCourseFromDataBase(mDataBase);
        ArrayList mCourseNames = new ArrayList<>();
        ArrayList mInstructorNames = new ArrayList<>();
        while (cursor.moveToNext()){
            mCourseNames.add(cursor.getString(cursor.getColumnIndex(AssignmentContract.CourseTable.COLUMN_COURSE_NAME)));
            mInstructorNames.add(cursor.getString(cursor.getColumnIndex(AssignmentContract.CourseTable.COLUMN_INSTUCTOR_NAME)));
        }
        data.add(mCourseNames);
        data.add(mInstructorNames);
        return data;
    }

    @Override
    public void onEditCourseClicked(final int position, final String oldCourseName, final String oldInstructorName) {
        View view = mRecyclerView.getChildAt(position);


        final RelativeLayout mInfoLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_course_edit_display);
        final RelativeLayout mEnterLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_course_edit_enter_text);

        final EditText courseNameEditText = (EditText) mEnterLayout.findViewById(R.id.edit_text_edit_course_item_Name);
        final EditText instructorNameEditText = (EditText) mEnterLayout.findViewById(R.id.edit_text_edit_course_item_Instructor);

        courseNameEditText.setText(oldCourseName);
        instructorNameEditText.setText(oldInstructorName);

        switchLayoutVisiblity(mInfoLayout,mEnterLayout);

        ImageView enterButton = (ImageView) mEnterLayout.findViewById(R.id.image_view_enter_button_course_edit);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AssignmentDataBaseHelper assignmentDataBaseHelper = new AssignmentDataBaseHelper(v.getContext());

                String newCourseName = courseNameEditText.getText().toString();
                String newInstructorName = instructorNameEditText.getText().toString();

                if (newCourseName.length() == 0) {
                    courseNameEditText.setError("Cannot be empty");
                    Toast.makeText(v.getContext(), "Please Enter a Course Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    assignmentDataBaseHelper.updateCourseName(mDataBase, oldCourseName,newCourseName);
                } catch (SQLiteConstraintException e){
                    Toast.makeText(v.getContext(), "Please Enter a Different Course Name", Toast.LENGTH_SHORT).show();
                    courseNameEditText.setError("A Course with this name has already been specified");
                    return;
                }
                assignmentDataBaseHelper.updateInstructorName(mDataBase, oldInstructorName, newInstructorName);

                mAdapter.mCourseNames.set(position,newCourseName);
                mAdapter.mInstructorNames.set(position,newInstructorName);
                mAdapter.notifyItemChanged(position);

                switchLayoutVisiblity(mInfoLayout,mEnterLayout);

            }
        });
    }

    private void switchLayoutVisiblity (RelativeLayout mInfoLayout, RelativeLayout mEnterLayout){
        if (mInfoLayout.getVisibility() == View.VISIBLE){
            mAddCourseButton.setVisibility(View.INVISIBLE);
            mInfoLayout.setVisibility(View.INVISIBLE);
            mEnterLayout.setVisibility(View.VISIBLE);
        }
        else {
            mAddCourseButton.setVisibility(View.VISIBLE);
            mInfoLayout.setVisibility(View.VISIBLE);
            mEnterLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDeleteCourseClicked(int position, String oldCourseName) {
        assignmentDataBaseHelper.deleteCourseFromDataBase(mDataBase,oldCourseName);
        mAdapter.mCourseNames.remove(position);
        mAdapter.mInstructorNames.remove(position);
        mAdapter.mNumberofItems = mAdapter.mCourseNames.size();
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(EditCourseActivity.this,MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void updateDataNewCourseDialog(String newCourseName, String newInstructorName) {
        mAdapter.mCourseNames.add(newCourseName);
        mAdapter.mInstructorNames.add(newInstructorName);
        mAdapter.mNumberofItems = mAdapter.mCourseNames.size();
        mAdapter.notifyDataSetChanged();
    }
}
