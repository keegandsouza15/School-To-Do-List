package com.example.keegan.schooltodolist.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keegan.schooltodolist.Data.AssignmentDataBaseHelper;
import com.example.keegan.schooltodolist.R;

import java.util.ArrayList;

/**
 * Created by Keegan on 7/19/2017.
 */

public class EditAssignmentDisplayAdapter extends RecyclerView.Adapter<EditAssignmentDisplayAdapter.EditAssignmentViewHolder>{

    Context mContext;
    SQLiteDatabase mDataBase;
    AssignmentDataBaseHelper assignmentDataBaseHelper;

    MyClickListeners myClickListeners;

    public  int mNumberOfItems;

    public ArrayList<String> mAssignmentNames;
    public ArrayList<Integer> mAssignmentIds;
    public ArrayList<String> mCourseNames;
    public ArrayList<Integer> areCompleted;

    public interface MyClickListeners {
        void onReinstateButtonClicked (View v, int assignmentId);
        void onEditButtonClicked (int assignmentId);
        void onDeleteButtonClicked (int assignmentId, int position);
    }


    public EditAssignmentDisplayAdapter (Context context, MyClickListeners listeners,
                                         SQLiteDatabase database,
                                         AssignmentDataBaseHelper _assignmentDataBaseHelper,
                                         ArrayList <String> assignmentNames,
                                         ArrayList <Integer> assignmentIds,
                                         ArrayList <String> courseNames,
                                         ArrayList <Integer> _areCompleted){
        mContext = context;
        myClickListeners  = listeners;
        assignmentDataBaseHelper = _assignmentDataBaseHelper;
        mDataBase = database;
        mAssignmentNames = assignmentNames;
        mAssignmentIds = assignmentIds;
        mCourseNames = courseNames;
        areCompleted = _areCompleted;

        mNumberOfItems = mAssignmentIds.size();
    }




    @Override
    public EditAssignmentDisplayAdapter.EditAssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.assignment_item_edit,parent, false);

        EditAssignmentViewHolder editAssignmentViewHolder = new EditAssignmentViewHolder(view);
        editAssignmentViewHolder.setClickListeners();
        return editAssignmentViewHolder;
    }

    @Override
    public void onBindViewHolder(EditAssignmentDisplayAdapter.EditAssignmentViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    public class EditAssignmentViewHolder extends RecyclerView.ViewHolder{

        TextView mAssignmentNameTextView;
        TextView mCourseNameTextView;

        Button mReinstateButton;
        ImageView mEditButton;
        ImageView mDeleteButton;


        public EditAssignmentViewHolder(View itemView) {
            super(itemView);

            mAssignmentNameTextView = (TextView) itemView.findViewById(R.id.assignment_item_edit_Name);
            mCourseNameTextView = (TextView) itemView.findViewById(R.id.assignment_item_edit_CourseName);

            mReinstateButton = (Button) itemView.findViewById(R.id.assignment_item_edit_reinstate_button);
            mEditButton = (ImageView) itemView.findViewById(R.id.assignment_item_edit_edit_button);
            mDeleteButton = (ImageView) itemView.findViewById(R.id.assignment_item_edit_delete_button);
        }

        void bind (int position){
            mAssignmentNameTextView.setText(mAssignmentNames.get(position));
            mCourseNameTextView.setText(mCourseNames.get(position));

            boolean finished =  !(areCompleted.get(position) == 0);
            if (!finished){
                mReinstateButton.setVisibility(View.INVISIBLE);
            }
        }

        void setClickListeners (){
            mReinstateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    myClickListeners.onReinstateButtonClicked(v, mAssignmentIds.get(adapterPosition));
                }
            });

            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    myClickListeners.onEditButtonClicked(mAssignmentIds.get(adapterPosition));
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    myClickListeners.onDeleteButtonClicked(mAssignmentIds.get(adapterPosition),  adapterPosition);
                }
            });
        }
    }
}
