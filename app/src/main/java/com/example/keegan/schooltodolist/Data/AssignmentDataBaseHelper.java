package com.example.keegan.schooltodolist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.keegan.schooltodolist.Data.AssignmentContract.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Keegan on 7/8/2017.
 */

public class  AssignmentDataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Assignment.db";

    public static final int DATABASE_VERSION = 8;

    public AssignmentDataBaseHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + AssignmentContract.CourseTable.TABLE_NAME  + "( " +
                CourseTable._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                CourseTable.COLUMN_COURSE_NAME + " TEXT NOT NULL UNIQUE," +
                CourseTable.COLUMN_INSTUCTOR_NAME + " TEXT" +
                " );";
        String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE " + AssignmentContract.AssignmentsTable.TABLE_NAME + "( " +
                AssignmentsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL," +
                AssignmentsTable.COLUMN_ASSIGNMENT_NAME + " TEXT NOT NULL UNIQUE," +
                AssignmentsTable.COLUMN_DUE_DATE + " TEXT NOT NULL," +
                AssignmentsTable.COLUMN_ESTIMATED_TIME + " INTEGER NOT NULL, " +
                AssignmentsTable.COLUMN_TIME_COMPLETED +" INTEGER NOT NULL, " +
                AssignmentsTable.COLUMN_PARTS + " INTEGER NOT NULL," +
                AssignmentsTable.COLUMN_PARTS_COMPLETED + " INTEGER NOT NULL," +
                AssignmentsTable.COLUMN_FINISHED + " INTEGER NOT NULL, " +
                AssignmentsTable.COLUMN_COURSE_ID + " INTEGER " +
                ");";

        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssignmentsTable.TABLE_NAME);
        onCreate(db);
    }

    public long insertAssignmentIntoDataBase (SQLiteDatabase db, String assignment_name, String due_date, int estimated_time, int time_completed, int parts, int parts_completed, int finished, int course_id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_ASSIGNMENT_NAME, assignment_name);
        contentValues.put(AssignmentsTable.COLUMN_DUE_DATE, due_date);
        contentValues.put(AssignmentsTable.COLUMN_ESTIMATED_TIME, estimated_time);
        contentValues.put(AssignmentsTable.COLUMN_TIME_COMPLETED, time_completed);
        contentValues.put(AssignmentsTable.COLUMN_PARTS_COMPLETED, parts_completed);
        contentValues.put(AssignmentsTable.COLUMN_PARTS, parts);
        contentValues.put(AssignmentsTable.COLUMN_PARTS_COMPLETED, parts_completed);
        contentValues.put(AssignmentsTable.COLUMN_FINISHED,finished);
        contentValues.put(AssignmentsTable.COLUMN_COURSE_ID,course_id);

        return db.insert(AssignmentsTable.TABLE_NAME, null, contentValues);
    }

    public long insertCourseIntoDataBase (SQLiteDatabase db, String course_name, String instructor){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CourseTable.COLUMN_COURSE_NAME, course_name);
        contentValues.put(CourseTable.COLUMN_INSTUCTOR_NAME, instructor);

        return db.insert(CourseTable.TABLE_NAME, null, contentValues);
    }

    public final int deleteCourseFromDataBase (SQLiteDatabase db, String name){
        String whereArgs [] = {name};
        String whereClause = CourseTable.COLUMN_COURSE_NAME + " =?";
        return db.delete(CourseTable.TABLE_NAME, whereClause, whereArgs);
    }

    public final int deleteAssignmentsFromDataBase (SQLiteDatabase db, int id){
        String whereArgs [] = {String.valueOf(id)};
        String whereClause = CourseTable._ID + " =?";
        return db.delete(AssignmentsTable.TABLE_NAME, whereClause, whereArgs);
    }

    public final Cursor getAllCourseFromDataBase (SQLiteDatabase db){
        return db.query(CourseTable.TABLE_NAME,null,null,null,null,null,null);
    }

    public final Cursor getAllAssignmentsFromDataBase (SQLiteDatabase db, boolean completed){
        if (completed){
            return db.query(AssignmentsTable.TABLE_NAME, null,null,null,null,null,null);
        }
        String whereClause = AssignmentsTable.COLUMN_FINISHED + "=?";
        String whereArgs [] ={"0"};
        return db.query(AssignmentsTable.TABLE_NAME, null,whereClause,whereArgs,null,null,null);
    }

    public final int getCourseIdFromDataBase (SQLiteDatabase db, String courseName){
        String selection [] = {CourseTable._ID};
        String whereArgs [] = {courseName};
        String whereClause = CourseTable.COLUMN_COURSE_NAME + " =?";

        Cursor c = db.query(CourseTable.TABLE_NAME,selection,whereClause,whereArgs ,null,null,null);
        if (!c.moveToFirst()){
            int no_course = -1;
            return no_course;
        };
        return  c.getInt(c.getColumnIndex(CourseTable._ID));
    }


    public final String getCourseNameFromAssignmentId (SQLiteDatabase db, int courseID){
        String query = "SELECT a." + CourseTable.COLUMN_COURSE_NAME +
                " FROM " + CourseTable.TABLE_NAME +" a" +
                " JOIN " + AssignmentsTable.TABLE_NAME +" b"+
                " WHERE " + "a."+CourseTable._ID  + "=" + courseID;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(CourseTable.COLUMN_COURSE_NAME));
        }
        else {
            return "null";
        }
    }

    public final String getDueNextDaysAssignments (SQLiteDatabase db, String due_date){
        StringBuilder assignmentNames = new StringBuilder();
        String whereClause = AssignmentsTable.COLUMN_DUE_DATE + "=?";
        String whereArgs [] = {due_date};
        Cursor c = db.query(AssignmentsTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        while (c.moveToNext()){
            assignmentNames.append(c.getString(c.getColumnIndex(AssignmentsTable.COLUMN_ASSIGNMENT_NAME)));
            assignmentNames.append(", ");
        }
        return assignmentNames.toString();

    }


    public final Cursor getAssignment (SQLiteDatabase db, int assignment_id){
        String table = AssignmentsTable.TABLE_NAME;
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        Cursor cursor = db.query(table,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return cursor;
    }

    public long updateCourseName (SQLiteDatabase db, String oldName, String newName) throws SQLException{
        String table = CourseTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CourseTable.COLUMN_COURSE_NAME,newName);
        String whereClause = CourseTable.COLUMN_COURSE_NAME + "=?";
        String whereArgs [] = {oldName};
        return db.update(table, contentValues, whereClause, whereArgs);
    }

    public long updateInstructorName (SQLiteDatabase db, String oldName, String newName){
        String table = CourseTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CourseTable.COLUMN_INSTUCTOR_NAME, newName);
        String whereClause = CourseTable.COLUMN_INSTUCTOR_NAME + "=?";
        String whereArgs [] = {oldName};
        return db.update(table,contentValues,whereClause,whereArgs);
    }

    public long updateTimeCompleted (SQLiteDatabase db, int assignment_id, int new_time){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_TIME_COMPLETED,new_time);
        String whereClause = AssignmentsTable._ID +"=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }

    public long updateAssignmentFinished (SQLiteDatabase db, int assignement_id, int finish){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_FINISHED, finish);
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs []= {String.valueOf(assignement_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }

    public String getAssignmentName (SQLiteDatabase db, int assignment_id){
        String table = AssignmentsTable.TABLE_NAME;
        String columns [] ={AssignmentsTable.COLUMN_ASSIGNMENT_NAME};
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        Cursor cursor = db.query(table,columns,whereClause,whereArgs,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(AssignmentsTable.COLUMN_ASSIGNMENT_NAME));
    }

    public String getDueDate (SQLiteDatabase db, int assignment_id){
        String table = AssignmentsTable.TABLE_NAME;
        String columns [] ={AssignmentsTable.COLUMN_DUE_DATE};
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        Cursor cursor = db.query(table,columns,whereClause,whereArgs,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(AssignmentsTable.COLUMN_DUE_DATE));
    }

    public int getEsitmatedTime (SQLiteDatabase db, int assignment_id){
        String table = AssignmentsTable.TABLE_NAME;
        String columns [] ={AssignmentsTable.COLUMN_ESTIMATED_TIME};
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        Cursor cursor = db.query(table,columns,whereClause,whereArgs,null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(AssignmentsTable.COLUMN_ESTIMATED_TIME));
    }
    public int getCourseId (SQLiteDatabase db, int assignment_id){
        String table = AssignmentsTable.TABLE_NAME;
        String columns [] ={AssignmentsTable.COLUMN_COURSE_ID};
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs [] = {String.valueOf(assignment_id)};
        Cursor cursor = db.query(table,columns,whereClause,whereArgs,null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(AssignmentsTable.COLUMN_COURSE_ID));
    }



    public long updateAssignmentName (SQLiteDatabase db, int assignment_id, String assignment_name){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_ASSIGNMENT_NAME, assignment_name);
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs []= {String.valueOf(assignment_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }

    public long updateDueDate (SQLiteDatabase db, int assignment_id, String due_date){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_DUE_DATE, due_date);
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs []= {String.valueOf(assignment_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }
    public long updateEstimatedTime (SQLiteDatabase db, int assignment_id, int estimated_time){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_ESTIMATED_TIME, estimated_time);
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs []= {String.valueOf(assignment_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }

    public long updateCourseId (SQLiteDatabase db, int assignment_id, int courseId){
        String table = AssignmentsTable.TABLE_NAME;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssignmentsTable.COLUMN_COURSE_ID, courseId);
        String whereClause = AssignmentsTable._ID + "=?";
        String whereArgs []= {String.valueOf(assignment_id)};
        return db.update(table,contentValues,whereClause,whereArgs);
    }


}
