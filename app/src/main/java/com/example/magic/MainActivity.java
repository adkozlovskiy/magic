package com.example.magic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView startGame;

    private MaterialCardView settings;

    private MaterialCardView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.start_game);
        settings = findViewById(R.id.settings);
        exit = findViewById(R.id.exit);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Нажата кнопка начала игры", Toast.LENGTH_SHORT).show();
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