package com.example.activitytest;

import android.graphics.Color;

public class TaskUiState
{
    private final String value1;
    private final String value2;
    private final String resultText;
    private final int resultBackgroundColor;
    private final boolean triggerSuccessAnimation;
    private final boolean triggerErrorAnimation;
    private final boolean requestFocus;

    public TaskUiState(String value1,
                       String value2,
                       String resultText,
                       int resultBackgroundColor,
                       boolean triggerSuccessAnimation,
                       boolean triggerErrorAnimation,
                       boolean requestFocus)
    {
        this.value1                  = value1;
        this.value2                  = value2;
        this.resultText              = resultText;
        this.resultBackgroundColor   = resultBackgroundColor;
        this.triggerSuccessAnimation = triggerSuccessAnimation;
        this.triggerErrorAnimation   = triggerErrorAnimation;
        this.requestFocus            = requestFocus;
    }

    // Standard-Initialzustand
    public TaskUiState()
    {
        this.value1 = "0";
        this.value2 = "0";
        this.resultText = "";
        this.resultBackgroundColor = Color.WHITE;
        this.triggerSuccessAnimation = false;
        this.triggerErrorAnimation = false;
        this.requestFocus = false;
    }

    public String getValue1() { return value1; }
    public String getValue2() { return value2; }
    public String getResultText() { return resultText; }
    public int getResultBackgroundColor() { return resultBackgroundColor; }
    public boolean shouldTriggerSuccessAnimation() { return triggerSuccessAnimation; }
    public boolean shouldTriggerErrorAnimation() { return triggerErrorAnimation; }
    public boolean shouldRequestFocus() { return requestFocus; }

    // HIER IST DIE FEHLENDE METHODE
    /**
     * Erstellt eine exakte Kopie des aktuellen Zustands, setzt aber den
     * Animations-Trigger gezielt auf 'false'.
     * Dies wird verwendet, um das Animations-Event als "verbraucht" zu markieren.
     * @return Ein neues TaskUiState-Objekt.
     */
    public TaskUiState copyWithAnimationConsumed()
    {
        return new TaskUiState(
                this.value1,
                this.value2,
                this.resultText,
                this.resultBackgroundColor,
                false,
                false,
                false
        );
    }
}
