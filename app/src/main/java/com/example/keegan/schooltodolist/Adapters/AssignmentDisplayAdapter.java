package com.example.keegan.schooltodolist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.keegan.schooltodolist.Data.AssignmentContract;
import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Keegan on 7/8/2017.
 */

public class AssignmentDisplayAdapter extends RecyclerView.Adapter <AssignmentDisplayAdapter.AssignmentDisplayViewHolder> {

    private int mNumberofItems;

    View mView;

    Context mContext;

    SQLiteDatabase mDataBase;

    ArrayList<String> assignmentItems;
    ArrayList<String> courseNames;
    ArrayList<Integer> courseIds;
    ArrayList<Integer> assignmentIds;

    ArrayList<Integer> esitmatedTimes;
    ArrayList<Integer> timeCompleted;
    ArrayList<Integer> timeLeft;

    AssignmentDataBaseHelper assignmentDataBaseHelper;

    AssignmentItemOnClickListener assignmentItemOnClickListener;

    RecyclerView mRecyclerView;

    public interface AssignmentItemOnClickListener{
        void onAssignmentClicked (int position);
    }

    public AssignmentDisplayAdapter(Context context, AssignmentItemOnClickListener listener, RecyclerView recyclerView) {

        mRecyclerView = recyclerView;

        assignmentItemOnClickListener = listener;

        mContext = context;

        assignmentDataBaseHelper = new AssignmentDataBaseHelper(context);

        mDataBase = assignmentDataBaseHelper.getReadableDatabase();

        // Finds the all the assignments;
        Cursor cursor = assignmentDataBaseHelper.getAllAssignmentsFromDataBase(mDataBase, false);

        // Code for sorting the objects
        class assignmentObject{
            public int id;
            public int time;
            public String due_date;

            assignmentObject (int _id, int _time, String _due_date){
                id = _id;
                time = _time;
                due_date = _due_date;
            }
        }

        ArrayList<assignmentObject> objects = new ArrayList<>();
        while (cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable._ID));
            int _etime = cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_ESTIMATED_TIME));
            int _ctime = cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_TIME_COMPLETED));
            int _time = _etime - _ctime;
            String _due_date = cursor.getString(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_DUE_DATE));
            assignmentObject object = new assignmentObject(_id,_time, _due_date);
            objects.add(object);
        }

        Collections.sort(objects,(new Comparator<assignmentObject>() {
            @Override
            public int compare(assignmentObject o1, assignmentObject o2) {
                Date date1 = new Date(o1.due_date);
                Date date2 = new Date(o2.due_date);
                int x = (date1.compareTo(date2));
                if ( (x == 1)){
                    return 1;
                }
                if ( x == -1){
                    return -1;
                }
                else {
                    if (o1.time > o2.time){
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        }));

        assignmentItems = new ArrayList<>();
        courseIds = new ArrayList<>();
        assignmentIds = new ArrayList<>();

        esitmatedTimes = new ArrayList<>();
        timeCompleted = new ArrayList<>();
        timeLeft = new ArrayList<>();

        cursor = null;
        for (int i = 0; i< objects.size(); i++) {
             cursor = assignmentDataBaseHelper.getAssignment(mDataBase, objects.get(i).id);

            while (cursor.moveToNext()) {
                courseIds.add(cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_COURSE_ID)));
                assignmentItems.add(cursor.getString(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_ASSIGNMENT_NAME)));
                assignmentIds.add(cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable._ID)));
                int a = cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_ESTIMATED_TIME));
                int b = cursor.getInt(cursor.getColumnIndex(AssignmentContract.AssignmentsTable.COLUMN_TIME_COMPLETED));
                esitmatedTimes.add(a);
                timeCompleted.add(b);
                timeLeft.add(a-b);
            }
        }





        mNumberofItems = assignmentItems.size();

        ArrayList<String> courseNamestemp = new ArrayList<>();

        // Finds the course names;
        String courseNameFromAssignmentId;
        for (int i = 0; i < assignmentItems.size(); i++){
            if (courseIds.get(i) != -1) {
                courseNameFromAssignmentId = assignmentDataBaseHelper.getCourseNameFromAssignmentId(mDataBase, courseIds.get(i));
            }
            else{
                courseNameFromAssignmentId = "";
            }
                courseNamestemp.add(courseNameFromAssignmentId);
        }

        courseNames = courseNamestemp;







    }

    @Override
    public AssignmentDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.assignment_item;
        LayoutInflater inflater =LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatlely = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediatlely);
        final AssignmentDisplayViewHolder assignmentDisplayViewHolder = new AssignmentDisplayViewHolder(view);


        mView = view;
        assignmentDisplayViewHolder.setListeners();

        return assignmentDisplayViewHolder;
    }

    @Override
    public void onBindViewHolder(AssignmentDisplayViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberofItems;
    }


    public class AssignmentDisplayViewHolder extends RecyclerView.ViewHolder {

        private TextView assignmentItem;
        private TextView mCourseNameTextView;
        private ImageView mAlertButton;

        TextView mTimeLeftTextView;

        SeekBar mSeekBar;

        Date currentDate;


        public AssignmentDisplayViewHolder(View itemView) {
            super(itemView);

            assignmentItem = (TextView) itemView.findViewById(R.id.text_view_assignment_item);
            mCourseNameTextView = (TextView) itemView.findViewById(R.id.text_view_assignment_item_course_name);
            mAlertButton = (ImageView) itemView.findViewById(R.id.button_due_today);

            mTimeLeftTextView = (TextView) itemView.findViewById(R.id.text_view_assignment_item_time_to_completion);

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            int year = date.getYear();
            int month = date.getMonth();
            int day = date.getDate();

            currentDate = new Date(year, month, day);


        }

        void setmTimeLeftTextView (int mTime){
            int hours = mTime/ 60;
            int mintues = mTime % 60;
            Time time = new Time(hours,mintues,0);
            mTimeLeftTextView.setText(time.toString());
        }

        void bind(final int list_index) {
            final int esitmated_time = esitmatedTimes.get(list_index);
            setmTimeLeftTextView(timeLeft.get(list_index));

            String due_date = assignmentDataBaseHelper.getDueDate(mDataBase,assignmentIds.get(list_index));
            Date date = new Date(due_date);
            if (currentDate.compareTo(date) == 0){
                mAlertButton.setVisibility(View.VISIBLE);
            }





            // Seek Bar Code
            mSeekBar = (SeekBar) itemView.findViewById(R.id.seek_bar_assignment_item);
            mSeekBar.setMax(esitmated_time);
            mSeekBar.setProgress(timeCompleted.get(list_index));



            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setmTimeLeftTextView(esitmated_time - progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    long a= assignmentDataBaseHelper.updateTimeCompleted(mDataBase,assignmentIds.get(list_index),seekBar.getProgress());
                    if (a == -1){
                        Log.e("Error","Data not entered");
                    }
                    AssignmentDisplayAdapter uassignmentDisplayAdapter = new AssignmentDisplayAdapter(mContext,assignmentItemOnClickListener,mRecyclerView);
                    mRecyclerView.setAdapter(uassignmentDisplayAdapter);
                    if (seekBar.getProgress() == seekBar.getMax()){
                        long b = assignmentDataBaseHelper.updateAssignmentFinished(mDataBase,assignmentIds.get(list_index),1);
                        AssignmentDisplayAdapter assignmentDisplayAdapter = new AssignmentDisplayAdapter(mContext,assignmentItemOnClickListener, mRecyclerView);
                        mRecyclerView.setAdapter(assignmentDisplayAdapter);
                        if (b == -1){
                            Log.e("Error","Data not entered");
                        }


                        else {
                            Log.v ("Data ", "Entered");
                        }
                    }
                }
            });

            assignmentItem.setText(assignmentItems.get(list_index));
            mCourseNameTextView.setText(courseNames.get(list_index));
        }

        public void setListeners (){
            mAlertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignmentItemOnClickListener.onAssignmentClicked(getAdapterPosition());
                }
            });






        }
    }
}
