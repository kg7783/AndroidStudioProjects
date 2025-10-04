package com.example.activitytest;

import java.util.ArrayList;

public class MultiplicationStrategy extends AbstractCalculationStrategy
{
    public MultiplicationStrategy(RandomUtils randomNumbers, ArrayList<Integer> allowedNumbers)
    {
        super(randomNumbers, allowedNumbers);
    }

    @Override
    public CalculationTask createNewTask()
    {
        int value1 = randomNumbers.getRandomNumberInRange10();
        int Value2 = randomNumbers.getRandomElementFromList(allowedNumbers);
        return new CalculationTask(value1, Value2);
    }

    @Override
    public int getExpectedResult(CalculationTask task)
    {
        return task.getValue1() * task.getValue2();
    }
}
