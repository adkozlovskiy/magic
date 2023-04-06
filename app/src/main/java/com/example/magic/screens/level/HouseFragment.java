package com.example.magic.screens.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magic.databinding.FragmentHomeBinding;
import com.example.magic.screens.GameActivity;
import com.example.magic.screens.GameView;

public class HouseFragment extends Fragment {

    private FragmentHomeBinding binding;

    private GameView gameView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.grandma.setOnClickListener(
                v -> {
                    ((GameActivity) getActivity()).getBinding().gameView.npcMove(binding.grandma.getX(), binding.grandma.getY() + 90, binding.grandma.getWidth(), binding.grandma.getHeight(), () -> {
                        ((GameActivity) getActivity()).getBinding().gameView.displayPlayerMessage("Привет!", binding.grandma.getX());
                    });
                }
        );
    }
}
