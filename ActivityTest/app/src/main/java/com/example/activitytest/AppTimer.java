package com.example.activitytest;

public interface AppTimer
{
    void start();
    void cancel();

    // Dies ist eine "Factory"-Schnittstelle, die uns hilft, den Timer zu erstellen,
    // ohne die Details preiszugeben.
    interface Factory
    {
        AppTimer create(long millisInFuture, long countDownInterval, Runnable onFinish);
    }
}
