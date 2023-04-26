package com.example.magic.screens.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magic.GameApplication;
import com.example.magic.databinding.FragmentMarketBinding;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.action.Action;
import com.example.magic.models.action.AddToInventory;
import com.example.magic.models.action.ChooseAction;
import com.example.magic.models.action.NextLevelAction;
import com.example.magic.models.action.NpcMessage;
import com.example.magic.models.action.RemoveFromInventory;
import com.example.magic.models.action.RunnableAction;
import com.example.magic.screens.GameActivity;
import com.example.magic.screens.MainActivity;
import com.example.magic.services.StorageManager;

import java.util.List;

public class MarketFragment extends Fragment {

    private FragmentMarketBinding binding;

    private StorageManager storageManager;

    private int potatoes = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storageManager = ((GameApplication) getActivity().getApplication()).getStorageManager();

        storageManager.level.observe(getViewLifecycleOwner(), l -> {
            if (storageManager.getGame().getForestUnlocked()) {
                ((GameActivity) getActivity()).setRightLocationVisible(View.VISIBLE);
            } else {
                ((GameActivity) getActivity()).setRightLocationVisible(View.GONE);
            }
            binding.oldMan.setOnClickListener(v -> {
                if (l == Level.GO_TO_SHOPPING) {
                    List<Action> grandmaActions = List.of(
                            new NpcMessage("Бери продукты с полок", binding.oldMan.getX(), binding.oldMan.getY()),
                            new RemoveFromInventory(Item.PRODUCTS),
                            new NextLevelAction(),
                            new RunnableAction(
                                    () -> {
                                        ((GameActivity) getActivity()).viewModel.startTimer(30);
                                    }
                            ),
                            new RunnableAction(
                                    this::startMimiGame
                            )
                    );
                    ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.oldMan.getX(), binding.oldMan.getY() + binding.oldMan.getHeight(), binding.oldMan.getWidth(), binding.oldMan.getHeight(), () -> {
                        ((GameActivity) getActivity()).getBinding().gameView.setUpActions(grandmaActions);
                    });
                } else if (l == Level.SHOPPING) {
                    ((GameActivity) getActivity()).viewModel.startTimer(30);
                    startMimiGame();
                } else if (l == Level.OLD_MAN) {
                    List<Action> actions = List.of(
                            new NpcMessage("Быстро справился. Неси маме", binding.oldMan.getX(), binding.oldMan.getY()),
                            new NpcMessage("И принеси мне таблеток, голова болит", binding.oldMan.getX(), binding.oldMan.getY()),
                            new NpcMessage("А я тебя яблоком угощу", binding.oldMan.getX(), binding.oldMan.getY()),
                            new ChooseAction(
                                    "Помочь старику?",
                                    () -> {
                                        storageManager.setHelpForOldMan(true);
                                        storageManager.nextLevel();
                                    },
                                    "Помочь",
                                    () -> {
                                        storageManager.setHelpForOldMan(false);
                                        storageManager.nextLevel();
                                    },
                                    "Отказать"
                            )
                    );
                    ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.oldMan.getX(), binding.oldMan.getY() + binding.oldMan.getHeight(), binding.oldMan.getWidth(), binding.oldMan.getHeight(), () -> {
                        ((GameActivity) getActivity()).getBinding().gameView.setUpActions(actions);
                    });
                } else if (l == Level.PILLS) {
                    List<Action> actions = List.of(
                            new NpcMessage("Спасибо!", binding.oldMan.getX(), binding.oldMan.getY()),
                            new NpcMessage("Вот. Как и обещал", binding.oldMan.getX(), binding.oldMan.getY()),
                            new AddToInventory(Item.GOLDEN_APPLE),
                            new NpcMessage("Иди дальше своей дорогой", binding.oldMan.getX(), binding.oldMan.getY()),
                            new NextLevelAction(),
                            new RunnableAction(() -> {
                                storageManager.unlockForest();
                            })

                    );
                    ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.oldMan.getX(), binding.oldMan.getY() + binding.oldMan.getHeight(), binding.oldMan.getWidth(), binding.oldMan.getHeight(), () -> {
                        ((GameActivity) getActivity()).getBinding().gameView.setUpActions(actions);
                    });
                }
            });
        });
    }

    private void stopGame() {
        ((GameActivity) getActivity()).viewModel.stopTimer();
        storageManager.nextLevel();
    }

    private void startMimiGame() {
        binding.potato1.setVisibility(View.VISIBLE);
        binding.potato2.setVisibility(View.VISIBLE);
        binding.potato3.setVisibility(View.VISIBLE);
        binding.potato4.setVisibility(View.VISIBLE);
        binding.potato5.setVisibility(View.VISIBLE);

        binding.potato1.setOnClickListener(p -> {
            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.potato1.getX(), binding.potato1.getY() + binding.potato1.getHeight(), binding.potato1.getWidth(), binding.potato1.getHeight(), () -> {
                potatoes++;
                binding.potato1.setVisibility(View.GONE);
                if (potatoes == 5) {
                    stopGame();
                }
            });
        });

        binding.potato2.setOnClickListener(p -> {
            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.potato2.getX(), binding.potato2.getY() + binding.potato2.getHeight(), binding.potato2.getWidth(), binding.potato2.getHeight(), () -> {
                potatoes++;
                binding.potato2.setVisibility(View.GONE);
                if (potatoes == 5) {
                    stopGame();
                }
            });
        });
        binding.potato3.setOnClickListener(p -> {
            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.potato3.getX(), binding.potato3.getY() + binding.potato3.getHeight(), binding.potato3.getWidth(), binding.potato3.getHeight(), () -> {
                potatoes++;
                binding.potato3.setVisibility(View.GONE);
                if (potatoes == 5) {
                    stopGame();
                }
            });
        });
        binding.potato4.setOnClickListener(p -> {
            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.potato4.getX(), binding.potato4.getY() + binding.potato4.getHeight(), binding.potato4.getWidth(), binding.potato4.getHeight(), () -> {
                potatoes++;
                binding.potato4.setVisibility(View.GONE);
                if (potatoes == 5) {
                    stopGame();
                }
            });
        });
        binding.potato5.setOnClickListener(p -> {
            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.potato5.getX(), binding.potato5.getY() + binding.potato5.getHeight(), binding.potato5.getWidth(), binding.potato5.getHeight(), () -> {
                potatoes++;
                binding.potato5.setVisibility(View.GONE);
                if (potatoes == 5) {
                    stopGame();
                }
            });
        });
    }
}
