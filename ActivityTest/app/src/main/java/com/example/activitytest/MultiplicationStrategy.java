package com.example.activitytest;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiplicationStrategy extends BaseCalculationStrategy
{
    public MultiplicationStrategy(TextView textValue1,
                                  TextView textValue2,
                                  EditText editResult,
                                  RandomUtils randomNumbers,
                                  ArrayList<Integer> allowedNumbers)
    {
        super(textValue1, textValue2, editResult, randomNumbers, allowedNumbers);
    }

    @Override
    public void createNewTask()
    {
        taskValue1 = randomNumbers.getRandomNumberInRange10();
        taskValue2 = randomNumbers.getRandomElementFromList(allowedNumbers);

        textValue1.setText(String.valueOf(taskValue1));
        textValue2.setText(String.valueOf(taskValue2));
    }

    @Override
    public void initTaskView()
    {
        editResult.setBackgroundColor(Color.WHITE);
        editResult.setText("");
    }

    @Override
    public int getExpectedResult()
    {
        return taskValue1*taskValue2;
    }
}
