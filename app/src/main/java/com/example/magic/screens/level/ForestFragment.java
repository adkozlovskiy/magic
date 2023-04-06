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
import com.example.magic.screens.GameActivity;
import com.example.magic.services.StorageManager;

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
                ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.egg.getX(), binding.egg.getY() - 170, binding.egg.getWidth(), binding.egg.getHeight(), () -> {
                    storageManager.addToInventory(Item.EGG);
                    binding.egg.setVisibility(View.GONE);
                    storageManager.nextLevel();
                });
            });
        } else {
            binding.egg.setVisibility(View.GONE);
        }
    }
}
