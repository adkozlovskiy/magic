package com.example.magic.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magic.R;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    private MaterialCardView change;

    private ImageView picture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        change = findViewById(R.id.select_background);
        picture = findViewById(R.id.picture);

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
        }
    }
}
