package com.example.activitytest;

import android.os.CountDownTimer;

public class AndroidTimer implements AppTimer
{
    private final CountDownTimer countDownTimer;

    private AndroidTimer(long millisInFuture, long countDownInterval, Runnable onFinish)
    {
        this.countDownTimer = new CountDownTimer(millisInFuture, countDownInterval)
        {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish()
            {
                onFinish.run();
            }
        };
    }

    @Override
    public void start()
    {
        countDownTimer.start();
    }

    @Override
    public void cancel()
    {
        countDownTimer.cancel();
    }

    // Die Factory, die echte AndroidTimer erstellt
    public static class Factory implements AppTimer.Factory
    {
        @Override
        public AppTimer create(long millisInFuture, long countDownInterval, Runnable onFinish)
        {
            return new AndroidTimer(millisInFuture, countDownInterval, onFinish);
        }
    }
}
