package com.example.activitytest;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CalculationViewModel extends ViewModel
{
    // Zufallsgenerator und erlaubte Zahlen
    private final RandomUtils randomNumbers = new RandomUtils();
    private ArrayList<Integer> allowedNumbers = new ArrayList<>();

    // Strategien für die Berechnungen (Model)
    private final CalculationStrategy multiplicationStrategy;
    private final CalculationStrategy divisionStrategy;

    // LiveData für den UI-Zustand der Multiplikationsaufgabe
    private final MutableLiveData<TaskUiState> _multiplicationState = new MutableLiveData<>(new TaskUiState());
    public final LiveData<TaskUiState> multiplicationState = _multiplicationState;

    // LiveData für den UI-Zustand der Divisionsaufgabe
    private final MutableLiveData<TaskUiState> _divisionState = new MutableLiveData<>(new TaskUiState());
    public final LiveData<TaskUiState> divisionState = _divisionState;

    // Interne Speicherung der aktuellen Aufgabe
    private CalculationTask currentMultiplicationTask;
    private CalculationTask currentDivisionTask;

    public CalculationViewModel()
    {
        multiplicationStrategy = new MultiplicationStrategy(randomNumbers, allowedNumbers);
        divisionStrategy = new DivisionStrategy(randomNumbers, allowedNumbers);
        Log.d("ViewModel", "ViewModel created");
    }

    public void setAllowedNumbers(ArrayList<Integer> numbers)
    {
        this.allowedNumbers.clear();
        this.allowedNumbers.addAll(numbers);

        // Nach dem Setzen der Zahlen direkt neue Aufgaben erstellen
        createNewMultiplicationTask();
        createNewDivisionTask();
    }

    // --- Public Methods for Multiplication ---

    public void createNewMultiplicationTask()
    {
        currentMultiplicationTask = multiplicationStrategy.createNewTask();
        _multiplicationState.setValue(new TaskUiState(
                String.valueOf(currentMultiplicationTask.getValue1()),
                String.valueOf(currentMultiplicationTask.getValue2()),
                "",
                Color.WHITE,
                false
        ));
    }

    public void checkMultiplicationResult(String userInput)
    {
        checkResult(userInput, currentMultiplicationTask, multiplicationStrategy, _multiplicationState, this::createNewMultiplicationTask);
    }

    // --- Public Methods for Division ---

    public void createNewDivisionTask()
    {
        currentDivisionTask = divisionStrategy.createNewTask();
        _divisionState.setValue(new TaskUiState(
                String.valueOf(currentDivisionTask.getValue1()),
                String.valueOf(currentDivisionTask.getValue2()),
                "",
                Color.WHITE,
                false
        ));
    }

    public void checkDivisionResult(String userInput)
    {
        checkResult(userInput,
                    currentDivisionTask,
                    divisionStrategy,
                    _divisionState,
                    this::createNewDivisionTask);
    }

    // --- Private Generic Logic ---

    private void checkResult(String userInput,
                             CalculationTask task,
                             CalculationStrategy strategy,
                             MutableLiveData<TaskUiState> state,
                             Runnable onSuccess)
    {
        if (userInput.isEmpty())
        {
            updateStateWithError(state, task, userInput);
            return;
        }

        try {
            int userResult = Integer.parseInt(userInput);
            int expectedResult = strategy.getExpectedResult(task);

            if (userResult == expectedResult)
            {
                // Korrekt
                updateStateWithSuccess(state, task, userInput);
                // Timer für den Übergang zur nächsten Aufgabe
                new CountDownTimer(1000, 1000)
                {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish()
                    {
                        onSuccess.run();
                    }
                }.start();
            }
            else
            {
                // Falsch
                updateStateWithError(state, task, userInput);
            }
        } catch (NumberFormatException e)
        {
            updateStateWithError(state, task, userInput);
        }
    }

    private void updateStateWithError(MutableLiveData<TaskUiState> state,
                                      CalculationTask task,
                                      String currentInput)
    {
        state.setValue(new TaskUiState(
                String.valueOf(task.getValue1()),
                String.valueOf(task.getValue2()),
                currentInput,
                Color.RED,
                false
        ));
    }

    private void updateStateWithSuccess(MutableLiveData<TaskUiState> state,
                                        CalculationTask task,
                                        String currentInput)
    {
        state.setValue(new TaskUiState(
                String.valueOf(task.getValue1()),
                String.valueOf(task.getValue2()),
                currentInput,
                Color.GREEN,
                true
        ));
    }

    // NEUE METHODE: Wird von der View aufgerufen, nachdem die Animation lief.
    public void onAnimationComplete(boolean isMultiplication)
    {
        if (isMultiplication)
        {
            TaskUiState currentState = _multiplicationState.getValue();
            if (currentState != null)
            {
                // Setze den Zustand zurück, aber ohne Animations-Trigger
                _multiplicationState.setValue(currentState.copyWithAnimationConsumed());
            }
        }
        else
        {
            TaskUiState currentState = _divisionState.getValue();
            if (currentState != null)
            {
                _divisionState.setValue(currentState.copyWithAnimationConsumed());
            }
        }
    }

}
