package com.example.magic.services;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.magic.R;

public class MediaManager {

    private Context context;

    private SoundPool soundPool;

    private int backgroundId;
    private int backgroundStreamId;

    private int stepsId;
    private int stepsStreamId;

    public MediaManager(Context context) {
        this.context = context;
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        backgroundId = soundPool.load(context, R.raw.background, 1);
        stepsId = soundPool.load(context, R.raw.steps, 1);
    }

    public void startBackgroundMusic() {
        soundPool.pause(backgroundStreamId);
        backgroundStreamId = soundPool.play(backgroundId, 0.2f, 0.2f, 1, 1, 1);
    }

    public void stopBackgroundMusic() {
        soundPool.pause(backgroundStreamId);
    }

    public void startSteps() {
        soundPool.pause(stepsStreamId);
        stepsStreamId = soundPool.play(stepsId, 1f, 1f, 1, 1, 1);
    }

    public void stopSteps() {
        soundPool.pause(stepsStreamId);
    }
}
