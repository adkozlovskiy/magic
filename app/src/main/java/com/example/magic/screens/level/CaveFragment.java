package com.example.magic.screens.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magic.GameApplication;
import com.example.magic.databinding.FragmentCaveBinding;
import com.example.magic.models.Game;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.action.Action;
import com.example.magic.models.action.NpcMessage;
import com.example.magic.models.action.RunnableAction;
import com.example.magic.models.action.UserMessage;
import com.example.magic.screens.GameActivity;
import com.example.magic.screens.GameView;
import com.example.magic.services.StorageManager;

import java.util.ArrayList;
import java.util.List;

public class CaveFragment extends Fragment {
    private StorageManager storageManager;
    private FragmentCaveBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storageManager = ((GameApplication) getActivity().getApplication()).getStorageManager();

        storageManager.level.observe(getViewLifecycleOwner(), l -> {
            GameActivity activity = ((GameActivity) getActivity());
            GameView gameView = activity.getBinding().gameView;
            activity.setRightLocationVisible(View.GONE);
            binding.koshey.setOnClickListener(v -> {
                if (l == Level.KOSHEY) {
                    List<Action> actions = List.of(
                            new NpcMessage("Что тебе нужно?", binding.koshey.getX(), binding.koshey.getY()),
                            new UserMessage("Меня прислала Баба Яга", binding.koshey.getX()),
                            new NpcMessage("Зачем?", binding.koshey.getX(), binding.koshey.getY()),
                            new UserMessage("Она сказала, у тебя есть лекартство", binding.koshey.getX()),
                            new UserMessage("Для моей мамы", binding.koshey.getX()),
                            new NpcMessage("Я потерял яйцо. Найди его, и поговорим", binding.koshey.getX(), binding.koshey.getY()),
                            new UserMessage("Хорошо", binding.koshey.getX()),
                            new RunnableAction(() -> {
                                storageManager.nextLevel();
                                activity.viewModel.startTimer(30);
                            })
                    );
                    gameView.npcMove(
                            binding.koshey.getX(),
                            binding.koshey.getY() + binding.koshey.getHeight(),
                            binding.koshey.getWidth(),
                            binding.koshey.getHeight(),
                            () -> {
                                gameView.setUpActions(actions);
                            }
                    );
                } else if (l == Level.RETURN_EGG) {
                    List<Action> actions = new ArrayList<>() {{
                        add(new NpcMessage("Давай сюда", binding.koshey.getX(), binding.koshey.getY()));
                        add(new RunnableAction(() -> {
                            activity.viewModel.stopTimer();
                            storageManager.removeForInventory(Item.EGG);
                        }));
                        add(new NpcMessage("Что у тебя еще есть?", binding.koshey.getX(), binding.koshey.getY()));
                    }};
                    if (storageManager.getGame().getItems().contains(Item.GOLDEN_APPLE)) {
                        actions.addAll(
                                List.of(
                                        new UserMessage("Есть золотое яблоко. Забирай!", binding.koshey.getX()),
                                        new NpcMessage("Сойдет", binding.koshey.getX(), binding.koshey.getY()),
                                        new RunnableAction(() -> {
                                            storageManager.removeForInventory(Item.GOLDEN_APPLE);
                                        }),
                                        new UserMessage("А лекарство?", binding.koshey.getX())
                                )
                        );
                    } else {
                        actions.addAll(
                                List.of(
                                        new UserMessage("Ничего!", binding.koshey.getX())
                                )
                        );
                    }
                    actions.addAll(
                            List.of(
                                    new NpcMessage("Ладно, забирай. Поспеши!", binding.koshey.getX(), binding.koshey.getY()),
                                    new RunnableAction(() -> {
                                        storageManager.addToInventory(Item.TANTUM_VERDE);
                                        storageManager.nextLevel();
                                        activity.viewModel.startTimer(20);
                                    })
                            )
                    );
                    gameView.npcMove(binding.koshey.getX(), binding.koshey.getY() + binding.koshey.getHeight(), binding.koshey.getWidth(), binding.koshey.getHeight(), () -> {
                        gameView.setUpActions(actions);
                    });
                }
            });
        });
    }
}
