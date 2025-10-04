package com.example.activitytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
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
    private Animation successAnimation;
    private Animation errorAnimation;

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

        setContentView(R.layout.activity_main);

        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        settingsManager = new SettingsManager(this);

        successAnimation = AnimationUtils.loadAnimation(this, R.anim.success_animation);
        errorAnimation   = AnimationUtils.loadAnimation(this, R.anim.error_animation);

        setupToolbarAndInsets();
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
        // Beobachter für die Multiplikations-UI einrichten
        observeTaskState(
                viewModel.multiplicationState,
                textValueMulti_1,
                textValueMulti_2,
                editResultMulti,
                true  // Flag, um die Multiplikation zu identifizieren
        );

        // Beobachter für die Divisions-UI einrichten
        observeTaskState(
                viewModel.divisionState,
                textValueDiv_1,
                textValueDiv_2,
                editResultDiv,
                false // Flag, um die Division zu identifizieren
        );
    }

    private void observeTaskState(
                LiveData<TaskUiState> stateLiveData,
                TextView textValue1,
                TextView textValue2,
                EditText editResult,
                boolean isMultiplicationTask)
    {
        // "Hey Anzeigetafel, sag mir immer Bescheid, wenn sich bei Multiplikation was ändert"
        // Beobachter für die Multiplikations-UI
        stateLiveData.observe(this, state ->
        {
            // Dieser Code wird JEDES MAL ausgeführt, wenn der Stadionsprecher etwas Neues sagt.
            // state ist hier der neue "Notizzettel" (TaskUiState)

            Log.d("Observer", "Updating UI for " + (isMultiplicationTask ? "multiplication" : "division"));

            // 1. Die Zahlenwerte aktualisieren
            textValue1.setText(state.getValue1());
            textValue2.setText(state.getValue2());

            // 2. Den Eingabetext aktualisieren (Cursor schützen)
            if (!editResult.isFocused())
            {
                editResult.setText(state.getResultText());
            }

            //3. Die HINTERGRUNDFÜLLUNG setzen (DIE WIRKLICH EINFACHE METHODE)
            Drawable background = editResult.getBackground();
            if (background instanceof LayerDrawable)
            {
                LayerDrawable layerDrawable = (LayerDrawable) background;

                // Wir holen uns die UNTERSTE Schicht (Index 0), was unsere Füllung ist.
                // Wir müssen sie nicht casten!
                Drawable fillLayer = layerDrawable.getDrawable(0);

                // Wir wenden den Farbfilter direkt auf diese Schicht an.
                // Wichtig: Wir brauchen hier .mutate(), damit die Änderung nur für dieses Feld gilt
                // und nicht für alle Felder, die dasselbe Drawable verwenden.
                if (state.getResultBackgroundColor() != android.graphics.Color.WHITE)
                {
                    fillLayer.mutate().setColorFilter(state.getResultBackgroundColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                else
                {
                    // Wenn die Farbe wieder weiß sein soll, entfernen wir den Filter.
                    fillLayer.mutate().clearColorFilter();
                }
            }

            // 4. Die Animation bei Erfolg auslösen
            if (state.shouldTriggerSuccessAnimation())
            {
                editResult.startAnimation(successAnimation);
                // Das ViewModel informieren, dass das Event verarbeitet wurde
                viewModel.onAnimationComplete(isMultiplicationTask);
            }

            // 5. Die Animation bei Fehler auslösen
            if (state.shouldTriggerErrorAnimation())
            {
                editResult.startAnimation(errorAnimation);
                // Das ViewModel informieren, dass das Event verarbeitet wurde
                viewModel.onAnimationComplete(isMultiplicationTask);
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

        String userInput = editResultMulti.getText().toString();
        viewModel.checkMultiplicationResult(userInput);

        // Fokus entziehen, damit das Feld beim nächsten Update geleert werden kann.
        editResultMulti.clearFocus();
    }

    public void onNewTaskDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTaskDiv");

        viewModel.createNewDivisionTask();
    }

    public void onCheckResultDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResultDiv");

        String userInput = editResultDiv.getText().toString();
        viewModel.checkDivisionResult(userInput);

        // NEU: Fokus entziehen, damit das Feld beim nächsten Update geleert werden kann.
        editResultDiv.clearFocus();
    }
}