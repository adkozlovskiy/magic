package com.example.magic;

import android.app.Application;

import com.example.magic.services.StorageManager;

public class GameApplication extends Application {

    private StorageManager storageManager;

    @Override
    public void onCreate() {
        super.onCreate();
        storageManager = new StorageManager(this);
        storageManager.gameLiveData.setValue(storageManager.getGame());
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
