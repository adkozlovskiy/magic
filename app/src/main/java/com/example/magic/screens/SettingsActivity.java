package com.example.magic.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.magic.GameApplication;
import com.example.magic.R;
import com.example.magic.services.MediaManager;
import com.example.magic.services.StorageManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    private MaterialCardView change;
    private StorageManager storageManager;
    private ImageView picture;

    private AppCompatCheckBox backgroundMusic;

    private ImageView returnButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        change = findViewById(R.id.select_background);
        picture = findViewById(R.id.picture);
        backgroundMusic = findViewById(R.id.background_music);
        returnButton = findViewById(R.id.return_button);

        storageManager = ((GameApplication) getApplication()).getStorageManager();
        MediaManager mediaManager = ((GameApplication) getApplication()).getMediaManager();

        // Чекаем музыку, если включена
        backgroundMusic.setChecked(storageManager.isMusicEnabled());

        backgroundMusic.setOnCheckedChangeListener((compoundButton, b) -> {
            storageManager.setMusicEnabled(b);
            if (b) {
                mediaManager.startBackgroundMusic();
            } else {
                mediaManager.stopBackgroundMusic();
            }
        });

        returnButton.setOnClickListener(v -> {
            onBackPressed();
        });

        change.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Выберите картинку"), 123);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri uri = data.getData();
            picture.setImageURI(uri);
            storageManager.saveBackgroundImage(uri.toString());
        }
    }
}
