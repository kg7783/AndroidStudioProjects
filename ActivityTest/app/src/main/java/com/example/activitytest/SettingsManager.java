package com.example.activitytest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SettingsManager
{
    private static final String PREFS_NAME = "CalculationAppSettings"; // Name der Einstellungsdatei
    private static final String SWITCH_KEY_PREFIX = "switch_"; // Präfix für die Schlüssel der Schalter

    private final SharedPreferences sharedPreferences;

    /**
     * Konstruktor, der den Kontext benötigt, um auf SharedPreferences zugreifen zu können.
     * @param context Der Anwendungskontext.
     */
    public SettingsManager(Context context)
    {
        // Holt eine Instanz von SharedPreferences, die privat für diese App ist.
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Speichert den Zustand eines einzelnen Schalters (z.B. für die Zahl 7).
     * @param switchNumber Die Nummer des Schalters (z.B. 7).
     * @param isChecked Der Zustand, der gespeichert werden soll (true für an, false für aus).
     */
    public void saveSwitchState(int switchNumber, boolean isChecked)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Erstellt einen eindeutigen Schlüssel wie "switch_7"
        String key = SWITCH_KEY_PREFIX + switchNumber;
        editor.putBoolean(key, isChecked);
        // Wendet die Änderungen an (speichert die Daten auf dem Gerät)
        editor.apply();
    }

    /**
     * Lädt den Zustand eines einzelnen Schalters.
     * @param switchNumber Die Nummer des Schalters, dessen Zustand geladen werden soll.
     * @return Der gespeicherte Zustand. Gibt 'true' als Standardwert zurück, falls noch nichts gespeichert wurde.
     */
    public boolean loadSwitchState(int switchNumber)
    {
        String key = SWITCH_KEY_PREFIX + switchNumber;
        // Lädt den booleschen Wert. Wenn der Schlüssel nicht existiert,
        // wird der Standardwert 'true' zurückgegeben.
        return sharedPreferences.getBoolean(key, true);
    }

    public ArrayList<Integer> loadSettings()
    {
        ArrayList<Integer> allowedNumbers = new ArrayList<>();
        // Geht alle relevanten Zahlenreihen durch (hier von 1 bis 10)
        for (int i = 1; i <= 10; i++)
        {
            // Verwendet die bereits vorhandene Methode, um den Zustand zu laden
            if (loadSwitchState(i))
            {
                // Wenn der Schalter für die Zahl 'i' an ist, füge 'i' zur Liste hinzu
                allowedNumbers.add(i);
            }
        }
        // Wenn kein einziger Schalter an ist, fügen wir einen Standardwert hinzu,
        // um einen Absturz in RandomUtils zu vermeiden (leere Liste).
        if (allowedNumbers.isEmpty())
        {
            allowedNumbers.add(2); // z.B. die 2er-Reihe als Fallback
        }
        return allowedNumbers;
    }


}
