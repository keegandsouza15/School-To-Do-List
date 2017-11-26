package com.example.keegan.schooltodolist.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.InflateException;
import android.widget.TextView;

import com.example.keegan.schooltodolist.Fragments.*;


import com.example.keegan.schooltodolist.R;

import java.util.ArrayList;

/**
 * Created by Keegan on 7/20/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        try {
            addPreferencesFromResource(R.xml.pref_general);

        }catch (InflateException e){
            Log.e("Error",e.getLocalizedMessage());
        }

    }
}
