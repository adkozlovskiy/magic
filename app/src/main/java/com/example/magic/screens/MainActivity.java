package com.example.magic.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.magic.R;
import com.example.magic.models.Game;
import com.example.magic.services.StorageManager;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView startGame;

    private ImageView settings;

    private MaterialCardView exit;

    private StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        storageManager = new StorageManager(MainActivity.this);

        startGame = findViewById(R.id.start_game);
        settings = findViewById(R.id.settings);
        exit = findViewById(R.id.exit);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageManager.newGame();
            }
        };

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
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