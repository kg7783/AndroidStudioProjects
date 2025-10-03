package com.example.activitytest;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DivisionStrategy extends AbstractCalculationStrategy
{
    public DivisionStrategy(RandomUtils randomNumbers, ArrayList<Integer> allowedNumbers)
    {
        super(randomNumbers, allowedNumbers);
    }

    @Override
    public CalculationTask createNewTask()
    {
        int value2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        // Sicherstellen, dass die Division immer eine ganze Zahl ergibt
        int value1 = value2 * randomNumbers.getRandomNumberInRange10();
        return new CalculationTask(value1, value2);
    }
    @Override
    public int getExpectedResult(CalculationTask task)
    {
        if(createNewTask().getValue2() == 0)
        {
            return 0;
        }

        return task.getValue1() / task.getValue2();
    }
}
