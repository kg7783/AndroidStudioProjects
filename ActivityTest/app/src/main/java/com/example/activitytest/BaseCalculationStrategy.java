package com.example.activitytest;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class BaseCalculationStrategy implements CalculationStrategy
{
    // 'protected' that sub classes have access
    protected final TextView textValue1, textValue2;
    protected final EditText editResult;
    protected final RandomUtils randomNumbers;
    protected final ArrayList<Integer> allowedNumbers;

    protected int taskValue1, taskValue2;


    protected BaseCalculationStrategy(TextView textValue1,
                                      TextView textValue2,
                                      EditText editResult,
                                      RandomUtils randomNumbers,
                                      ArrayList<Integer> allowedNumbers)
    {
        this.textValue1     = textValue1;
        this.textValue2     = textValue2;
        this.editResult     = editResult;
        this.randomNumbers  = randomNumbers;
        this.allowedNumbers = allowedNumbers;
    }

    /**
     * Diese Methode hat für alle Strategien die gleiche Implementierung
     * und wird daher von der Basisklasse bereitgestellt.
     */
    @Override
    public void initTaskView()
    {
        editResult.setBackgroundColor(Color.WHITE);
        editResult.setText("");
    }

    // Die folgenden Methoden müssen von den konkreten Subklassen
    // implementiert werden, da ihre Logik spezifisch ist.
    @Override
    public abstract void createNewTask();

    @Override
    public abstract int getExpectedResult();
}
