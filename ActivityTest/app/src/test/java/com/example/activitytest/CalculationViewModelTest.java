package com.example.activitytest;

import android.content.Context;
import android.graphics.Color;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class) // Notwendig, wenn man Mocking-Frameworks wie Mockito verwendet.
public class CalculationViewModelTest
{
    // Diese Regel ist SEHR WICHTIG!
    // Sie sorgt dafür, dass LiveData-Operationen sofort und synchron ausgeführt werden,
    // anstatt im Hintergrund auf dem Android-Main-Thread.
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // NEU: Mocks für unsere neuen Abhängigkeiten
    @Mock
    private AppTimer.Factory mockTimerFactory;
    @Mock
    private AppTimer mockTimer;

    // Das zu testende Objekt
    private CalculationViewModel viewModel;

    // Diese Methode wird vor JEDEM einzelnen Test ausgeführt.
    // So stellen wir sicher, dass jeder Test mit einem "frischen" ViewModel startet.
    @Before
    public void setUp()
    {
        // Sag dem Mock, was er tun soll: Wenn create() aufgerufen wird, gib unseren mockTimer zurück.
        when(mockTimerFactory.create(anyLong(), anyLong(), any(Runnable.class)))
                .thenReturn(mockTimer);

        // Erstelle das ViewModel und INJIZIERE die Mock-Factory
        viewModel = new CalculationViewModel(mockTimerFactory);
    }

    // Ein Testfall wird mit der @Test-Annotation markiert.
    // Der Name sollte beschreiben, was getestet wird: "wasPassiert_wennManDasTut_sollteDiesesErgebnisRauskommen"
    @Test
    public void createNewMultiplicationTask_whenAllowedNumbersAreSet_updatesLiveDataCorrectly()
    {
        // ARRANGE (Vorbereiten)
        // Definiere die erlaubten Zahlen
        ArrayList<Integer> allowedNumbers = new ArrayList<>(Arrays.asList(5));
        viewModel.setAllowedNumbers(allowedNumbers);

        // ACT (Ausführen)
        // Die Methode setAllowedNumbers ruft intern bereits createNew...Task auf.
        // Wir holen uns den aktuellen Zustand aus dem LiveData.
        TaskUiState state = viewModel.multiplicationState.getValue();

        // ASSERT (Überprüfen)
        // Überprüfen, ob der Zustand nicht null ist
        Assert.assertNotNull(state);
        // Überprüfen, ob der zweite Wert aus unserer Liste der erlaubten Zahlen kommt.
        Assert.assertEquals("5", state.getValue2());
        // Überprüfen, ob der Hintergrund anfangs weiß ist.
        Assert.assertEquals(Color.WHITE, state.getResultBackgroundColor());
    }

    @Test
    public void checkMultiplicationResult_withCorrectInput_setsStateToSuccess() {
        // ARRANGE (Vorbereiten)
        // Erstelle eine Aufgabe. Wir nehmen hier einen Trick: Da wir die Zufallszahlen nicht steuern können,
        // holen wir uns die erstellte Aufgabe und berechnen das Ergebnis selbst.
        viewModel.setAllowedNumbers(new ArrayList<>(Arrays.asList(7))); // z.B. nur die 7er-Reihe
        TaskUiState initial_state = viewModel.multiplicationState.getValue();
        Assert.assertNotNull(initial_state);
        int value1 = Integer.parseInt(initial_state.getValue1());
        int value2 = Integer.parseInt(initial_state.getValue2());
        int correctResult = value1 * value2;

        // ACT (Ausführen)
        // Gib die korrekte Antwort ein und prüfe.
        viewModel.checkMultiplicationResult(String.valueOf(correctResult));

        // ASSERT (Überprüfen)
        TaskUiState final_state = viewModel.multiplicationState.getValue();
        Assert.assertNotNull(final_state);
        // Der Hintergrund sollte jetzt GRÜN sein.
        Assert.assertEquals(Color.GREEN, final_state.getResultBackgroundColor());
        // Die Animation sollte ausgelöst werden.
        Assert.assertTrue(final_state.shouldTriggerSuccessAnimation());
    }

    @Test
    public void checkMultiplicationResult_withWrongInput_setsStateToError() {
        // ARRANGE
        viewModel.setAllowedNumbers(new ArrayList<>(Arrays.asList(3)));
        TaskUiState initial_state = viewModel.multiplicationState.getValue();
        Assert.assertNotNull(initial_state);
        int value1 = Integer.parseInt(initial_state.getValue1());
        int value2 = Integer.parseInt(initial_state.getValue2());
        int correctResult = value1 * value2;
        int wrongResult = correctResult + 1; // Garantiert falsch

        // ACT
        viewModel.checkMultiplicationResult(String.valueOf(wrongResult));

        // ASSERT
        TaskUiState final_state = viewModel.multiplicationState.getValue();
        Assert.assertNotNull(final_state);
        // Der Hintergrund sollte jetzt ROT sein.
        Assert.assertEquals(Color.RED, final_state.getResultBackgroundColor());
    }
}
