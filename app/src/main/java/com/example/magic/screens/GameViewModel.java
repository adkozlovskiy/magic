package com.example.magic.screens;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {

    private CountDownTimer timer;

    public MutableLiveData<Long> timerData = new MutableLiveData<>();

    public void startTimer(long seconds) {
        timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerData.setValue(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timerData.setValue(0L);
                stopTimer();
            }
        }.start();
    }

    public void stopTimer() {
        timer.cancel();
        timer = null;
    }
}
