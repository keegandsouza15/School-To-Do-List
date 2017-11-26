package com.example.keegan.schooltodolist.Pickers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.keegan.schooltodolist.R;

import java.util.Date;

/**
 * Created by Keegan on 7/12/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button dateButton;

    public Date date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        dateButton = (Button) getActivity().findViewById(R.id.button_choose_due_date);


        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        date = new Date(year-1900,month,dayOfMonth,0,0);
        setDateButtonColor();
    }

    public Date getDate(){
        return date;
    }

    private void setDateButtonColor (){
        dateButton.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
        dateButton.setTextColor(Color.WHITE);
        dateButton.setError(null);
        dateButton.setText(date.toString());
    }

}

