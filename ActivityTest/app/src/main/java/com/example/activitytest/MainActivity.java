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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private SettingsManager settingsManager;

    private final RandomUtils randomNumbers = new RandomUtils();
    ArrayList<Integer> allowedNumbers = new ArrayList<Integer>();

    // Variable for multiplication
    private Integer taskValueMulti_1, taskValueMulti_2;
    private TextView textValueMulti_1, textValueMulti_2;
    private EditText editResultMulti;

    // Variable for division
    private Integer taskValueDiv_1, taskValueDiv_2;
    private TextView textValueDiv_1, textValueDiv_2;
    private EditText editResultDiv;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Erstelle eine Instanz des SettingsManagers
        settingsManager = new SettingsManager(this);

        // Multiplication
        textValueMulti_1  = findViewById(R.id.textValueMulti_1);
        textValueMulti_2  = findViewById(R.id.textValueMulti_2);
        editResultMulti   = findViewById(R.id.editResultMulti);

        // Division
        textValueDiv_1  = findViewById(R.id.textValueDiv_1);
        textValueDiv_2  = findViewById(R.id.textValueDiv_2);
        editResultDiv   = findViewById(R.id.editResultDiv);
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

        InitMultiplication();
        InitDivision();

        createNewMultiplication();
        createNewDivision();
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
    protected void onDestroy(){
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

        allowedNumbers.clear();

        // Create an array of the resource IDs for easier looping
        int[] switchIds = {
                R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4, R.id.switch5,
                R.id.switch6, R.id.switch7, R.id.switch8, R.id.switch9, R.id.switch10
        };

        for (int i = 0; i < SettingsActivity.NUMBER_OF_SWITCHES; i++)
        {
            boolean isChecked = settingsManager.loadSwitchState(i + 1);
            if(isChecked)
            {
                allowedNumbers.add(i+1);
            }
        }

        Log.d("MainActivityTest", "Allowed numbers: " + allowedNumbers.toString());
    }

    private void InitMultiplication()
    {
        // Multiplication result
        editResultMulti.setBackgroundColor(Color.WHITE);
        editResultMulti.setText("");
    }

    private void InitDivision()
    {
        // Division result
        editResultDiv.setBackgroundColor(Color.WHITE);
        editResultDiv.setText("");
    }
    @SuppressLint("SetTextI18n")
    private void createNewMultiplication()
    {
        // Get random value from range 0-10
        taskValueMulti_1 = randomNumbers.getRandomNumberInRange10();
        textValueMulti_1.setText(taskValueMulti_1.toString());

        // Get random value from settings
        taskValueMulti_2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        textValueMulti_2.setText(taskValueMulti_2.toString());
    }

    public void onNewTaskMulti(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTaskMulti");

        InitMultiplication();
        createNewMultiplication();
    }

    public void onCheckResultMulti(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResultMulti");

        checkResult(editResultMulti, taskValueMulti_1*taskValueMulti_2, () ->
        {
            InitMultiplication();;
            createNewMultiplication();
        });
    }

    @SuppressLint("SetTextI18n")
    private void createNewDivision()
    {
        // // Get random values
        taskValueDiv_2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        taskValueDiv_1 = taskValueDiv_2 * randomNumbers.getRandomNumberInRange10();

        textValueDiv_1.setText(Integer.toString((taskValueDiv_1)));
        textValueDiv_2.setText(taskValueDiv_2.toString());
    }

    public void onNewTaskDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTaskDiv");

        InitDivision();
        createNewDivision();;
    }

    public void onCheckResultDiv(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResultDiv");

        checkResult(editResultDiv, taskValueDiv_1/taskValueDiv_2, () ->
        {
            InitDivision();
            createNewDivision();
        });
    }

    private void checkResult(EditText editResult, int expectedResult, Runnable onSuccess)
    {
        String str = editResult.getText().toString();
        if(str.isEmpty())
        {
            return;
        }

        int editTextResult = Integer.parseInt(str);
        if(editTextResult == expectedResult)
        {
            editResult.setBackgroundColor(Color.GREEN);

            new CountDownTimer(1000, 1000)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    Log.d("MainActivityTest", "onTick");
                }
                @Override
                public void onFinish()
                {
                    Log.d("MainActivityTest", "onFinish");

                    if(onSuccess != null)
                    {
                        onSuccess.run();
                    }

/*
                    if(editResult == editResultMulti)
                    {
                        InitMultiplication();;
                        createNewMultiplication();;
                    }
                    else if(editResult == editResultDiv)
                    {
                        InitDivision();
                        createNewDivision();
                    }
  */
                }
            }.start();
        }
        else
        {
            editResult.setBackgroundColor(Color.RED);
        }
    }
}