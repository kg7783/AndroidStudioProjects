package com.example.activitytest;

public interface CalculationStrategy
{
    /**
     * Erstellt eine neue Rechenaufgabe und gibt die Werte zurück.
     * @return Ein CalculationTask-Objekt mit den neuen Werten.
     */
    CalculationTask createNewTask();

    /**
     * Berechnet das erwartete Ergebnis basierend auf den übergebenen Werten.
     * @param task Die aktuelle Aufgabe mit value1 und value2.
     * @return Das korrekte Ergebnis der Berechnung.
     */
    int getExpectedResult(CalculationTask task);
}
