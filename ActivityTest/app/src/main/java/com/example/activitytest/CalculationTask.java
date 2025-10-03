package com.example.activitytest;

// Eine einfache Datenklasse (POJO), um die Werte einer Aufgabe zu halten.
public class CalculationTask
{
    private final int value1, value2;

    public CalculationTask(int value1, int value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue1()
    {
        return value1;
    }

    public int getValue2()
    {
        return value2;
    }
}
