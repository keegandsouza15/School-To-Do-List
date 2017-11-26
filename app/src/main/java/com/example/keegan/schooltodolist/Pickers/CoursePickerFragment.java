package com.example.keegan.schooltodolist.Pickers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.keegan.schooltodolist.R;

import java.util.ArrayList;

/**
 * Created by Keegan on 7/12/2017.
 */

public class CoursePickerFragment extends DialogFragment {

    private Button mBackButton;
    private Button mEnterButton;
    private Button mCourseChoiceButton;

    private ArrayList<String> mCourseNames;
    private String mChoosenCourseName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {

        getDialog().getWindow().setLayout(500, 500);

        View view;
        view = inflater.inflate(R.layout.course_chooser, container, false);

        final Bundle args = getArguments();
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_course_choose);

       mCourseNames = args.getStringArrayList("Array_List");

        for (int i = 0; i < mCourseNames.size(); i++) {
           RadioButton radioButton = new RadioButton(getActivity());
           radioButton.setId(i);
           radioButton.setText(mCourseNames.get(i));
           radioGroup.addView(radioButton);
       }


        mBackButton = (Button) view.findViewById(R.id.button_back_fragment_course_chooser);
        mEnterButton = (Button) view.findViewById(R.id.button_back_fragment_course_chooser);
        mCourseChoiceButton = (Button) getActivity().findViewById(R.id.button_choose_course);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mEnterButton = (Button) view.findViewById(R.id.button_enter_fragment_course_chooser);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if (radioButtonId == -1){
                    return;
                }
                mChoosenCourseName = mCourseNames.get(radioButtonId);
                mCourseChoiceButton.setError(null);
                mCourseChoiceButton.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                mCourseChoiceButton.setText(mChoosenCourseName);
                mCourseChoiceButton.setTextColor(Color.WHITE);
                dismiss();
            }
        });


        return view;
    }

    public String getmChoosenCourseName (){
        return mChoosenCourseName;
    }
}
