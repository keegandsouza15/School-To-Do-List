package com.example.keegan.schooltodolist.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.keegan.schooltodolist.R;

import java.util.ArrayList;

import static android.R.attr.button;
import static android.R.attr.onClick;
import static android.R.attr.viewportHeight;

/**
 * Created by Keegan on 7/17/2017.
 */

public class EditCourseDisplayAdapter extends RecyclerView.Adapter<EditCourseDisplayAdapter.EditCourseViewHolder> {
    public int mNumberofItems;

    public ArrayList<String> mCourseNames;
    public ArrayList<String> mInstructorNames;

    Context mContext;


    CourseEditOnClickListener mCourseEditOnClickListener;

    public interface CourseEditOnClickListener {
        void onEditCourseClicked (int position, String oldCourse, String oldInstructor);
        void onDeleteCourseClicked (int position, String oldCourseName);
    }


    public EditCourseDisplayAdapter(Context context,CourseEditOnClickListener listener, ArrayList <String> courseNames,ArrayList <String> instructorNames){
        mContext = context;
        mCourseEditOnClickListener = listener;
        mCourseNames = courseNames;
        mInstructorNames =instructorNames;
        mNumberofItems = mCourseNames.size();
    }

    @Override
    public EditCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.edit_course_item,parent,false);

        EditCourseViewHolder editCourseViewHolder = new EditCourseViewHolder(view);

        editCourseViewHolder.setOnClickListeners();

        return editCourseViewHolder;
    }

    @Override
    public void onBindViewHolder(EditCourseViewHolder holder, int position) {
        Log.v("Called","onBindViewHolder");
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberofItems;
    }

    public class EditCourseViewHolder extends RecyclerView.ViewHolder {

        TextView CourseNameDisplay;
        TextView InstructorNameDisplay;

        ImageView editCourseButton;
        ImageView deleteCourseButton;

        public EditCourseViewHolder(View itemView) {
            super(itemView);

            // Course Name and Instructor Name Text Views
            CourseNameDisplay = (TextView) itemView.findViewById(R.id.text_view_edit_course_item_Name);
            InstructorNameDisplay = (TextView) itemView.findViewById(R.id.text_view_edit_course_item_Instructor);

            // Edit Button
            editCourseButton  = (ImageView) itemView.findViewById(R.id.image_view_edit_button_course_edit);

            // Delete Button
            deleteCourseButton = (ImageView) itemView.findViewById(R.id.image_view_delete_button_course_edit);
        }

        void bind (int position){
            CourseNameDisplay.setText(mCourseNames.get(position));
            InstructorNameDisplay.setText(mInstructorNames.get(position));
        }

       public void setOnClickListeners (){
            // Edit button onClickListener
            editCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String currentCourseName = mCourseNames.get (position);
                    String currentInstructorName = mInstructorNames.get (position);
                    mCourseEditOnClickListener.onEditCourseClicked(position,currentCourseName, currentInstructorName);
                }
            });

            // Delete button onClickListener
            deleteCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String currentCourseName = mCourseNames.get (position);
                    mCourseEditOnClickListener.onDeleteCourseClicked(position, currentCourseName);
                }
            });
        }
    }
}

