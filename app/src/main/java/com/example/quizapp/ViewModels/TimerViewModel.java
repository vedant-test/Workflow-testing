package com.example.quizapp.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class TimerViewModel extends ViewModel {
    public static final int SECOND = 1000;
    private MutableLiveData<Long> elapsedTime = new MutableLiveData<Long>();
    private int initialTime;
    private Timer timer;

    public TimerViewModel() {
//        total time for exam = 30 minutes
        initialTime = 300*6;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long newValue = initialTime - 1;
                initialTime -= 1;
                if (initialTime == 0) {
                    timer.cancel();
                }
                elapsedTime.postValue(newValue);
            }
        }, SECOND, SECOND);
    }
//    getting the lie data
    public LiveData<Long> getElapsedTime() {
        return elapsedTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer.cancel();
    }
}
