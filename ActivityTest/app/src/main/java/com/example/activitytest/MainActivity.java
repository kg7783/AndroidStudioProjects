package com.example.activitytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private CalculationViewModel viewModel;
    private SettingsManager settingsManager;

    // UI-Komponenten
    private TextView textValueMulti_1, textValueMulti_2;
    private EditText editResultMulti;
    private TextView textValueDiv_1, textValueDiv_2;
    private EditText editResultDiv;
    private Animation pulsateAnimation; // Animations-Objekt

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("MainActivityTest", "onCreate");

        if(savedInstanceState != null)
        {
            int value = savedInstanceState.getInt("Test");
            Log.d("MainActivityTest", "value = " + value);
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Without this, menu will not work
        Toolbar toolbar = findViewById(R.id.toolbar_Main);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ViewModel holen - dies überlebt Konfigurationsänderungen
        viewModel = new ViewModelProvider(this).get(CalculationViewModel.class);

        setupToolbarAndInsets();

        settingsManager = new SettingsManager(this);

        // Lade die Animation einmalig in onCreate
        pulsateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulsate_animation);

        initializeViews();

        setupObservers();
    }

    private void setupToolbarAndInsets()
    {
        Toolbar toolbar = findViewById(R.id.toolbar_Main);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews()
    {
        textValueMulti_1    = findViewById(R.id.textValueMulti_1);
        textValueMulti_2    = findViewById(R.id.textValueMulti_2);
        editResultMulti     = findViewById(R.id.editResultMulti);

        textValueDiv_1      = findViewById(R.id.textValueDiv_1);
        textValueDiv_2      = findViewById(R.id.textValueDiv_2);
        editResultDiv       = findViewById(R.id.editResultDiv);
    }

    // Hier wird die Verbindung zwischen ViewModel und View hergestellt
    private void setupObservers()
    {
        // Beobachter für die Multiplikations-UI
        viewModel.multiplicationState.observe(this, state ->
        {
            Log.d("Observer", "Updating multiplication UI");
            textValueMulti_1.setText(state.getValue1());
            textValueMulti_2.setText(state.getValue2());
            // Nur aktualisieren, wenn es nicht im Fokus ist, um den Cursor zu schützen
            if (!editResultMulti.isFocused())
            {
                editResultMulti.setText(state.getResultText());
            }
            editResultMulti.setBackgroundColor(state.getResultBackgroundColor());

            // NEU: Animation starten, falls getriggert
            if (state.shouldTriggerSuccessAnimation())
            {
                editResultMulti.startAnimation(pulsateAnimation);
                // Informiere das ViewModel, dass das Event verarbeitet wurde.
                viewModel.onAnimationComplete(true);
            }
        });

        // Beobachter für die Divisions-UI
        viewModel.divisionState.observe(this, state ->
        {
            Log.d("Observer", "Updating division UI");
            textValueDiv_1.setText(state.getValue1());
            textValueDiv_2.setText(state.getValue2());
            if (!editResultDiv.isFocused())
            {
                editResultDiv.setText(state.getResultText());
            }
            editResultDiv.setBackgroundColor(state.getResultBackgroundColor());

            // NEU: Animation starten, falls getriggert
            if (state.shouldTriggerSuccessAnimation())
            {
                editResultDiv.startAnimation(pulsateAnimation);
                // Informiere das ViewModel, dass das Event verarbeitet wurde.
                viewModel.onAnimationComplete(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Test", 15);
        Log.d("MainActivityTest", "savedInstanceState");
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MainActivityTest", "onRestoreInstanceState");

        int value = savedInstanceState.getInt("Test");
        Log.d("MainActivityTest", "value = " + value);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("MainActivityTest", "onStart");

        LoadSettings();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d("MainActivityTest", "onRestart");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("MainActivityTest", "onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("MainActivityTest", "onResume");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("MainActivityTest", "onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("MainActivityTest", "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("MainActivityTest", "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.menu_settings)
        {
            Toast.makeText(this, "Menu Settings activated", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadSettings()
    {
        Log.d("MainActivityTest", "LoadSettings");

        ArrayList<Integer> allowedNumbers = settingsManager.loadSettings();
        viewModel.setAllowedNumbers(allowedNumbers);

        Log.d("MainActivityTest", "Allowed numbers: " + allowedNumbers.toString());
    }

    public void onNewTaskMulti(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTaskMulti");

        viewModel.createNewMultiplicationTask();
    }
    public void onCheckResultMulti(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResultMulti");

        viewModel.checkMultiplicationResult(editResultMulti.getText().toString());
    }

    public void onNewTaskDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTaskDiv");

        viewModel.createNewDivisionTask();
    }

    public void onCheckResultDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResultDiv");

        viewModel.checkDivisionResult(editResultDiv.getText().toString());
    }
}