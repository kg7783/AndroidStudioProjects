package com.example.activitytest;

public interface CalculationStrategy
{
    /**
     * Erstellt eine neue Rechenaufgabe und zeigt sie in den TextViews an.
     */
    void createNewTask();

    /**
     * Initialisiert die UI-Komponenten für eine neue Runde (z.B. Hintergrundfarbe zurücksetzen).
     */
    void initTaskView();

    /**
     * Gibt das erwartete korrekte Ergebnis der aktuellen Aufgabe zurück.
     * @return Das Ergebnis der Berechnung.
     */
    int getExpectedResult();
}
