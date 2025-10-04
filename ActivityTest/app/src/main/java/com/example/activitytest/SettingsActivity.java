package com.example.activitytest;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    static final int NUMBER_OF_SWITCHES = 10;
    private SwitchCompat[] switches;
    private SettingsManager settingsManager; // Member-Variable für den Manager


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Without this, menu will not work
        Toolbar toolbar = findViewById(R.id.toolbar_Settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("SettingsActivityTest", "onCreate");

        // Erstelle eine Instanz des SettingsManagers
        settingsManager = new SettingsManager(this);

        // 2. Initialize the array
        switches = new SwitchCompat[NUMBER_OF_SWITCHES];

        // Load settings
        LoadSettings();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("SettingsActivityTest", "savedInstanceState");
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("SettingsActivityTest", "onRestoreInstanceState");

        int value = savedInstanceState.getInt("Test");
        Log.d("SettingsActivityTest", "value = " + value);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("SettingsActivityTest", "onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d("SettingsActivityTest", "onRestart");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("SettingsActivityTest", "onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("SettingsActivityTest", "onResume");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("SettingsActivityTest", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("SettingsActivityTest", "onDestroy");
    }

    public void onSaveSettings(View view)
    {
        Log.d("SettingsActivityTest", "onSaveSettings");

        SaveSettings();

        finish();
    }

    public void LoadSettings()
    {
        // Create an array of the resource IDs for easier looping
        int[] switchIds = {
                R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4, R.id.switch5,
                R.id.switch6, R.id.switch7, R.id.switch8, R.id.switch9, R.id.switch10
        };

        for (int i = 0; i < NUMBER_OF_SWITCHES; i++)
        {
            // Find the switch by its ID and add it to the array
            switches[i] = findViewById(switchIds[i]);

            // Nutze den SettingsManager, um den Zustand zu laden
            boolean isChecked = settingsManager.loadSwitchState(i + 1);
            switches[i].setChecked(isChecked);
        }
    }

    private void SaveSettings()
    {
        // Save the state of each switch using a loop
        for (int i = 0; i < NUMBER_OF_SWITCHES; i++)
        {
            // Nutze den SettingsManager, um den Zustand zu speichern
            settingsManager.saveSwitchState(i + 1, switches[i].isChecked());
        }
    }

}