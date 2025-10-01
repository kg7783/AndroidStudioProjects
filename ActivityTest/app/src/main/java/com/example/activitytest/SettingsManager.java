package com.example.activitytest;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager
{
    private static final String PREF_NAME = "SETTINGS";
    private final SharedPreferences sharedPref;

    public SettingsManager(Context context)
    {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSwitchState(int switchNumber, boolean isChecked)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        String prefKey = "switch_" + switchNumber;
        editor.putBoolean(prefKey, isChecked);
        editor.apply(); // apply() ist asynchron und performanter als commit()
    }

    public boolean loadSwitchState(int switchNumber)
    {
        String prefKey = "switch_" + switchNumber;
        // Als Standardwert nehmen wir 'true', wie in deinem ursprünglichen Code
        return sharedPref.getBoolean(prefKey, true);
    }
}
