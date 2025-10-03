package com.example.activitytest;

import java.util.ArrayList;

public abstract class AbstractCalculationStrategy implements CalculationStrategy
{
    // Die Strategie kennt nur noch die Hilfsklassen, keine Views mehr.
    protected final RandomUtils randomNumbers;
    protected final ArrayList<Integer> allowedNumbers;

    public AbstractCalculationStrategy(RandomUtils randomNumbers, ArrayList<Integer> allowedNumbers)
    {
        this.randomNumbers = randomNumbers;
        this.allowedNumbers = allowedNumbers;
    }

    // Die konkrete Logik bleibt in den Subklassen.
    @Override
    public abstract CalculationTask createNewTask();

    @Override
    public abstract int getExpectedResult(CalculationTask task);
}
