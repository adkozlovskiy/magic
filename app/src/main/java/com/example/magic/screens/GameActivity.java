package com.example.magic.screens;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.magic.GameApplication;
import com.example.magic.R;
import com.example.magic.databinding.ActivityGameBinding;
import com.example.magic.models.Location;
import com.example.magic.services.StorageManager;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    private GameViewModel viewModel;

    private StorageManager storageManager;

    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        storageManager = ((GameApplication) getApplication()).getStorageManager();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                AlertDialog dialog = builder.setTitle("Вы действительно хотите выйти?")
                        .setMessage("Прогресс будет сохранен автоматически")
                        .setPositiveButton("Выйти", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            finish();
                        })
                        .setNegativeButton("Отмена", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                        })
                        .create();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                //Show the dialog!
                dialog.show();

                //Set the dialog to immersive sticky mode
                dialog.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        binding.exit.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.rightLocation.setOnClickListener(v -> {
            storageManager.rightLocation();
        });

        binding.leftLocation.setOnClickListener(v -> {
            storageManager.leftLocation();
        });

        binding.inventory.setOnClickListener(v -> {
            navController.navigate(R.id.inventoryFragment);
        });

        storageManager.gameLiveData.observe(
                this,
                game -> {
                    if (game.getHealth() >= 1) {
                        binding.heartOne.setAlpha(1f);

                        if (game.getHealth() >= 2) {
                            binding.heartTwo.setAlpha(1f);

                            if (game.getHealth() >= 3) {
                                binding.heartThree.setAlpha(1f);
                            } else {
                                binding.heartThree.setAlpha(0.3f);
                            }
                        } else {
                            binding.heartTwo.setAlpha(0.3f);
                            binding.heartThree.setAlpha(0.3f);
                        }
                    } else {
                        // end game
                    }

                    if (game.getLastLocation() == Location.HOME) {
                        binding.leftLocation.setVisibility(View.GONE);
                    } else {
                        binding.leftLocation.setVisibility(View.VISIBLE);
                    }

                    if (game.getLastLocation() == Location.CAVE) {
                        binding.rightLocation.setVisibility(View.GONE);
                    } else {
                        binding.rightLocation.setVisibility(View.VISIBLE);
                    }

                    if (game.getLastLocation() == Location.HOME) {
                        navController.navigate(R.id.houseFragment);
                    } else if (game.getLastLocation() == Location.MARKET) {
                        navController.navigate(R.id.marketFragment);
                    } else if (game.getLastLocation() == Location.CAVE) {
                        navController.navigate(R.id.caveFragment);
                    } else if (game.getLastLocation() == Location.FOREST) {
                        navController.navigate(R.id.forestFragment);
                    }
                });
    }
}
