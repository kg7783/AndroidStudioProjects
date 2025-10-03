package com.example.activitytest;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DivisionStrategy extends BaseCalculationStrategy
{
    public DivisionStrategy(TextView textValue1,
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
        taskValue2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        // Sicherstellen, dass die Division immer eine ganze Zahl ergibt
        taskValue1 = taskValue2 * randomNumbers.getRandomNumberInRange10();

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
        if(taskValue2 == 0)
        {
            return 0;
        }

        return taskValue1/taskValue2;
    }
}
