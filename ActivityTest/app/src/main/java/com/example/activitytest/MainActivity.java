package com.example.activitytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

    private Integer taskValue1, taskValue2;
    private TextView textValue1, textValue2;
    private EditText editResult;


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

        textValue1  = findViewById(R.id.textValue1);
        textValue2  = findViewById(R.id.textValue2);
        editResult  = findViewById(R.id.editResult);
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

        Init();
        createNewMultiplication();
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

    private void Init()
    {
        editResult.setBackgroundColor(Color.WHITE);
        editResult.setText("");
    }
    @SuppressLint("SetTextI18n")
    private void createNewMultiplication()
    {
        // Get random value from range 0-10
        taskValue1 = randomNumbers.getRandomNumberInRange10();
        textValue1.setText(taskValue1.toString());

        // Get random value from settings
        taskValue2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        textValue2.setText(taskValue2.toString());
    }

    public void onNewTask(android.view.View view)
    {
        Log.d("MainActivityTest", "onNewTask");

        Init();
        createNewMultiplication();
    }

    public void onCheckResult(android.view.View view)
    {
        Log.d("MainActivityTest", "onCheckResult");

        int iResult = Integer.parseInt(editResult.getText().toString());
        if (iResult == taskValue1 * taskValue2)
        {
            editResult.setBackgroundColor(Color.GREEN);
        }
        else
        {
            editResult.setBackgroundColor(Color.RED);
        }
    }

}