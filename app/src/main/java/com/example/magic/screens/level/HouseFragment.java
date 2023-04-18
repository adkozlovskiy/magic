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
import com.example.magic.models.action.UserMessage;
import com.example.magic.screens.GameActivity;
import com.example.magic.screens.GameView;
import com.example.magic.services.StorageManager;

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
        binding.grandma.setOnClickListener(
                v -> {
                    if (storageManager.getGame().getCurrentLevel() == Level.MUM) {
                        List<Action> grandmaActions = List.of(
                                new UserMessage("Привет!", binding.grandma.getX()),
                                new NpcMessage("Доброе утро, сходи за покупками", binding.grandma.getX(), binding.grandma.getY()),
                                new AddToInventory(Item.PRODUCTS),
                                new NextLevelAction()
                        );
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
    }
}
