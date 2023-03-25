package com.example.magic.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.magic.GameApplication;
import com.example.magic.R;
import com.example.magic.models.Game;
import com.example.magic.services.StorageManager;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView startGame;

    private ImageView settings;

    private MaterialCardView exit;

    private MaterialCardView _continue;

    private StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        storageManager = ((GameApplication) getApplication()).getStorageManager();

        startGame = findViewById(R.id.start_game);
        settings = findViewById(R.id.settings);
        exit = findViewById(R.id.exit);
        _continue = findViewById(R.id.load_game);

        _continue.setOnClickListener(v -> {
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


        startGame.setOnClickListener(listener);
        settings.setOnClickListener(listener2);
        exit.setOnClickListener(
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