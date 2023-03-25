package com.example.magic.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.magic.databinding.FragmentInventoryBinding;
import com.example.magic.models.Item;

import java.util.List;

public class InventoryFragment extends DialogFragment {

    private FragmentInventoryBinding binding;

    private InventoryAdapter adapter = new InventoryAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.items.setAdapter(adapter);
        adapter.updateInventory(
                List.of(
                        Item.EGG, Item.EGG
                )
        );
        binding.close.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
