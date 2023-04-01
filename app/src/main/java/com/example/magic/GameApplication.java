package com.example.magic;

import android.app.Application;

import com.example.magic.services.MediaManager;
import com.example.magic.services.StorageManager;

public class GameApplication extends Application {

    private StorageManager storageManager;

    private MediaManager mediaManager;

    @Override
    public void onCreate() {
        super.onCreate();
        storageManager = new StorageManager(this);
        storageManager.gameLiveData.setValue(storageManager.getGame());

        mediaManager = new MediaManager(this);
        if (storageManager.isMusicEnabled()) {
            mediaManager.startBackgroundMusic();
        }
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }
}
