package com.example.keegan.schooltodolist.Data;

import android.provider.BaseColumns;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Keegan on 7/8/2017.
 */

public class AssignmentContract {

    AssignmentContract(){};

    public static class CourseTable implements BaseColumns {
        public static final String TABLE_NAME = "Courses";
        public static final String COLUMN_COURSE_NAME = "name";
        public static final String COLUMN_INSTUCTOR_NAME = "instructor";
    }

    public static class AssignmentsTable implements BaseColumns {

        public static final String TABLE_NAME = "Assignments";
        public static final String COLUMN_ASSIGNMENT_NAME = "name";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_ESTIMATED_TIME = "estimated_time";
        public static final String COLUMN_TIME_COMPLETED = "time_completed";
        public static final String COLUMN_PARTS = "parts";
        public static final String COLUMN_PARTS_COMPLETED = "parts_completed";
        public static final String COLUMN_FINISHED = "finished";
        public static final String COLUMN_COURSE_ID = "course_id";
    }
}
