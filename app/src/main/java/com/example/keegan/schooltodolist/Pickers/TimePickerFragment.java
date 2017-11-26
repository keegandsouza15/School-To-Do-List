package com.example.keegan.schooltodolist.Pickers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.keegan.schooltodolist.R;

import java.sql.Time;

/**
 * Created by Keegan on 7/12/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Time time;

    private Button timeButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int startHour = 0;
        int startMintue = 0;
        boolean isTwentyFourHourFormat = true;

        timeButton = (Button) getActivity().findViewById(R.id.button_choose_estimated_time);

        return  new TimePickerDialog(getActivity(), this, startHour, startMintue, isTwentyFourHourFormat);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = new Time(hourOfDay,minute,0);
        Log.v("Time Picker", time.toString());
        setTimeButtonColor();
    }

    public Time getTime (){
        return time;
    }

    private void setTimeButtonColor (){
        timeButton.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
        timeButton.setTextColor(Color.WHITE);

        timeButton.setError(null);
        timeButton.setText(time.toString());
    }
}
