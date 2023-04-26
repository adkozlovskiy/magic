package com.example.magic.screens.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magic.GameApplication;
import com.example.magic.databinding.FragmentHomeBinding;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.action.Action;
import com.example.magic.models.action.AddToInventory;
import com.example.magic.models.action.NextLevelAction;
import com.example.magic.models.action.NpcMessage;
import com.example.magic.models.action.RemoveFromInventory;
import com.example.magic.models.action.RunnableAction;
import com.example.magic.models.action.UserMessage;
import com.example.magic.screens.GameActivity;
import com.example.magic.screens.GameView;
import com.example.magic.services.StorageManager;

import java.util.ArrayList;
import java.util.List;

public class HouseFragment extends Fragment {

    private FragmentHomeBinding binding;

    private GameView gameView;

    private StorageManager storageManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageManager = ((GameApplication) getActivity().getApplication()).getStorageManager();
        storageManager.level.observe(getViewLifecycleOwner(), l -> {
            ((GameActivity) getActivity()).setRightLocationVisible(View.VISIBLE);
            binding.grandma.setOnClickListener(
                    v -> {
                        if (l == Level.MUM) {
                            List<Action> grandmaActions = List.of(
                                    new UserMessage("Привет!", binding.grandma.getX()),
                                    new NpcMessage("Доброе утро, сходи за покупками", binding.grandma.getX(), binding.grandma.getY()),
                                    new AddToInventory(Item.PRODUCTS),
                                    new NextLevelAction()
                            );
                            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.grandma.getX(), binding.grandma.getY() + binding.grandma.getHeight(), binding.grandma.getWidth(), binding.grandma.getHeight(), () -> {
                                ((GameActivity) getActivity()).getBinding().gameView.setUpActions(grandmaActions);
                            });
                        } else if (l == Level.HEALTH_MUM) {
                            List<Action> actions = List.of(
                                    new UserMessage("Поправляйся!", binding.grandma.getX()),
                                    new NpcMessage("Спасибо!", binding.grandma.getX(), binding.grandma.getY()),
                                    new RemoveFromInventory(Item.TANTUM_VERDE),
                                    new NextLevelAction()
                            );
                            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.grandma.getX(), binding.grandma.getY() + binding.grandma.getHeight(), binding.grandma.getWidth(), binding.grandma.getHeight(), () -> {
                                ((GameActivity) getActivity()).getBinding().gameView.setUpActions(actions);
                            });
                        } else if (l == Level.PUT_PRODUCTS) {
                            ArrayList<Action> grandmaActions = new ArrayList<Action>() {{
                                add(new NpcMessage("Спасибо!", binding.grandma.getX(), binding.grandma.getY()));
                                add(new UserMessage("Пожалуйста", binding.grandma.getX()));
                            }};
                            if (storageManager.getGame().getHelpForOldMan()) {
                                grandmaActions.add(
                                        new UserMessage("Дай таблетки для старика", binding.grandma.getX())
                                );
                                grandmaActions.add(
                                        new NpcMessage("Конечно!", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new AddToInventory(Item.PILLS)
                                );
                                grandmaActions.add(
                                        new NpcMessage("Слушай, а сходи потом...", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new NpcMessage("... проведай бабу ягу в лесу", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new RunnableAction(() -> {
                                            storageManager.setLevel(Level.PILLS);
                                        })
                                );
                            } else {
                                grandmaActions.add(
                                        new NpcMessage("Слушай, а сходи как еще...", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new NpcMessage("... проведай бабу ягу в лесу", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new NpcMessage("Только поторопись", binding.grandma.getX(), binding.grandma.getY())
                                );
                                grandmaActions.add(
                                        new RunnableAction(() -> {
                                            storageManager.unlockForest();
                                        })
                                );
                                grandmaActions.add(
                                        new RunnableAction(() -> {
                                            ((GameActivity) getActivity()).viewModel.startTimer(50);
                                        })
                                );
                                grandmaActions.add(new RunnableAction(() -> {
                                    storageManager.setLevel(Level.BABA_YAGA);
                                }));
                            }
                            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.grandma.getX(), binding.grandma.getY() + binding.grandma.getHeight(), binding.grandma.getWidth(), binding.grandma.getHeight(), () -> {
                                ((GameActivity) getActivity()).getBinding().gameView.setUpActions(grandmaActions);
                            });
                        } else {
                            List<Action> grandmaActions = List.of(
                                    new NpcMessage("Ты уже сходил за продуктами?", binding.grandma.getX(), binding.grandma.getY()));
                            ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.grandma.getX(), binding.grandma.getY() + binding.grandma.getHeight(), binding.grandma.getWidth(), binding.grandma.getHeight(), () -> {
                                ((GameActivity) getActivity()).getBinding().gameView.setUpActions(grandmaActions);
                            });
                        }
                    }
            );
        });

    }
}
