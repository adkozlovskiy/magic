package com.example.magic.screens;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magic.databinding.ItemItemBinding;
import com.example.magic.models.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<Item> inventory = new ArrayList<>();

    public void updateInventory(List<Item> inventory) {
        this.inventory.clear();
        this.inventory.addAll(inventory);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(
                ItemItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.bind(inventory.get(position));
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    class InventoryViewHolder extends RecyclerView.ViewHolder {

        private ItemItemBinding binding;

        public InventoryViewHolder(ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Item item) {
            binding.image.setImageResource(item.getResId());
        }
    }
}
