package com.example.keegan.schooltodolist.Pickers;

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.R;

/**
 * Created by Keegan on 7/25/2017.
 */

public class NewCourseDialog extends DialogFragment {

    private EditText courseNameEditText;
    private EditText instructorEditText;

    private ImageView courseEnterButton;
    private ImageView exitButton;

    private SQLiteDatabase mDataBase;
    private AssignmentDataBaseHelper assignmentDataBaseHelper;

    updateDataNewCourseDialog updateData;

    public interface updateDataNewCourseDialog {
        void updateDataNewCourseDialog (String newCourseName, String newInstructorName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.new_course_dialog,container);

        courseNameEditText = (EditText) view.findViewById(R.id.edit_text_course_name);
        instructorEditText = (EditText) view.findViewById(R.id.edit_text_instructor_name);

        ImageView courseEnterButton = (ImageView) view.findViewById(R.id.button_course_enter);
        ImageView exitButton = (ImageView) view.findViewById(R.id.button_exit_course_dialog);

        courseEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_name = courseNameEditText.getText().toString();
                String instructor = instructorEditText.getText().toString();

                if (course_name.length() <= 0){
                    courseNameEditText.setError("Enter a course name");
                    Toast.makeText(getActivity(), "Please Speify a course name", Toast.LENGTH_SHORT).show();
                    return;
                }
                long inserted = assignmentDataBaseHelper.insertCourseIntoDataBase(mDataBase, course_name, instructor);
                if (inserted == -1){
                    courseNameEditText.setError("A course with this name has been entered already");
                    Toast.makeText(getActivity(),"Please enter a different course name",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(),"Course Entered",Toast.LENGTH_SHORT).show();
                updateData.updateDataNewCourseDialog(course_name,instructor);
                getDialog().dismiss();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void setDataBase (SQLiteDatabase dataBase, AssignmentDataBaseHelper helper, updateDataNewCourseDialog method){
        updateData= method;
        mDataBase = dataBase;
        assignmentDataBaseHelper = helper;
    }
}
