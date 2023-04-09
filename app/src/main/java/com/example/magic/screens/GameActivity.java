package com.example.magic.screens;

import android.os.Bundle;
import android.util.Log;
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
import com.example.magic.models.Transition;
import com.example.magic.services.StorageManager;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    public ActivityGameBinding getBinding() {
        return binding;
    }

    private GameViewModel viewModel;

    private StorageManager storageManager;

    private NavController navController;

    private Location currentLocation;

    private Transition eventTransition = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel.startTimer(60);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    if (destination.getId() == R.id.caveFragment) {
                        currentLocation = Location.CAVE;
                    } else if (destination.getId() == R.id.houseFragment) {
                        currentLocation = Location.HOME;
                    } else if (destination.getId() == R.id.forestFragment) {
                        currentLocation = Location.FOREST;
                    } else if (destination.getId() == R.id.marketFragment) {
                        currentLocation = Location.MARKET;
                    }
                }
        );

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
            binding.gameView.moveToRightLocation(() -> {
                Log.d("TAG", "MOVED");
                storageManager.rightLocation(currentLocation);
            });
        });

        binding.leftLocation.setOnClickListener(v -> {
            binding.gameView.moveToLeftLocation(() -> {
                Log.d("TAG", "MOVED");
                storageManager.leftLocation(currentLocation);
            });
        });

        binding.inventory.setOnClickListener(v -> {
            Log.d("TAG", "MOVED");
            navController.navigate(R.id.inventoryFragment);
        });

        viewModel.timerData.observe(this, s -> {
            long minutes = s / 60;
            long seconds = s % 60;

            String text = minutes + ":" + seconds;
            binding.timer.setText(text);

            if (s <= 10) {
                binding.timer.setTextColor(
                        getColor(R.color.red)
                );
            }else {
                binding.timer.setTextColor(
                        getColor(R.color.white)
                );
            }

            if (s == 0) {
                // TODO game over
                storageManager.gameOver(false);
            }
        });

        storageManager.heath.observe(
                this,
                health -> {
                    if (health >= 1) {
                        binding.heartOne.setAlpha(1f);

                        if (health >= 2) {
                            binding.heartTwo.setAlpha(1f);

                            if (health >= 3) {
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


                });
        storageManager.transition.observe(
                this,
                transition -> {
                    if (transition == null || transition.getTo() == null) {
                        return;
                    }
                    Log.d("TAG", "TRANSITION " + transition.getTo() + " " + transition.getFrom());
                    if ((eventTransition != null && Objects.equals(eventTransition.getFrom(), transition.getFrom())) && Objects.equals(eventTransition.getTo(), transition.getTo())) {
                        return;
                    }

                    Log.d("TAG", "EV TRA " + eventTransition);
                    eventTransition = transition;

                    if (transition.getTo() == Location.HOME) {
                        binding.leftLocation.setVisibility(View.GONE);
                    } else {
                        binding.leftLocation.setVisibility(View.VISIBLE);
                    }

                    if (transition.getTo() == Location.CAVE) {
                        binding.rightLocation.setVisibility(View.GONE);
                    } else {
                        binding.rightLocation.setVisibility(View.VISIBLE);
                    }

                    if (transition.getTo() == Location.HOME) {
                        if (transition.getFrom() == null) {
                            binding.gameView.positionPlayerLeft();
                        } else {
                            binding.gameView.positionPlayerRight();
                        }
                        navController.navigate(R.id.houseFragment);
                    } else if (transition.getTo() == Location.MARKET) {
                        if (transition.getFrom() == Location.HOME) {
                            binding.gameView.positionPlayerLeft();
                        } else {
                            binding.gameView.positionPlayerRight();
                        }
                        navController.navigate(R.id.marketFragment);
                    } else if (transition.getTo() == Location.CAVE) {
                        if (transition.getFrom() == Location.FOREST) {
                            binding.gameView.positionPlayerLeft();
                        } else {
                            binding.gameView.positionPlayerRight();
                        }
                        navController.navigate(R.id.caveFragment);
                    } else if (transition.getTo() == Location.FOREST) {
                        if (transition.getFrom() == Location.MARKET) {
                            binding.gameView.positionPlayerLeft();
                        } else {
                            binding.gameView.positionPlayerRight();
                        }
                        navController.navigate(R.id.forestFragment);
                    }
                }
        );
    }
}
