package com.example.magic.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magic.GameApplication;
import com.example.magic.R;
import com.example.magic.databinding.ActivityMainBinding;
import com.example.magic.services.StorageManager;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private StorageManager storageManager;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);
        storageManager = ((GameApplication) getApplication()).getStorageManager();

        binding.loadGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        View.OnClickListener listener = view -> {
            storageManager.newGame();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        };

        View.OnClickListener listener2 = view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        };


        binding.startGame.setOnClickListener(listener);
        binding.settings.setOnClickListener(listener2);
        binding.exit.setOnClickListener(
                v -> {
                    exit();
                }
        );
    }

    private void exit() {
        // TODO
        finish();
    }
}