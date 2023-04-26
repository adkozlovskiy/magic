package com.example.magic.screens.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magic.GameApplication;
import com.example.magic.databinding.FragmentForestBinding;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.action.Action;
import com.example.magic.models.action.AddToInventory;
import com.example.magic.models.action.NextLevelAction;
import com.example.magic.models.action.NpcMessage;
import com.example.magic.models.action.UserMessage;
import com.example.magic.screens.GameActivity;
import com.example.magic.services.StorageManager;

import java.util.List;

public class ForestFragment extends Fragment {

    private FragmentForestBinding binding;

    private StorageManager storageManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storageManager = ((GameApplication) getActivity().getApplication()).getStorageManager();
        if (storageManager.getGame().getCurrentLevel() == Level.EGG) {
            binding.egg.setVisibility(View.VISIBLE);
            binding.egg.setOnClickListener(v -> {
                List<Action> eggActions = List.of(
                        new AddToInventory(Item.EGG),
                        new NextLevelAction()
                );
                ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.egg.getX(), binding.egg.getY() + binding.egg.getHeight(), binding.egg.getWidth(), binding.egg.getHeight(), () -> {
                    binding.egg.setVisibility(View.GONE);
                    ((GameActivity) getActivity()).getBinding().gameView.setUpActions(eggActions);
                });
            });
        } else {
            binding.egg.setVisibility(View.GONE);
        }
    }
}
