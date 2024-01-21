package com.example.jkk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jkk.AllMenu;
import com.example.jkk.databinding.ItemItemsBinding;

import java.util.ArrayList;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.AdminItemUploadDeleteViewHolder> {

    private final Context context;
    private final ArrayList<AllMenu> menuList;
    private final ArrayList<Integer> itemQuantities;

    public AddItemAdapter(Context context, ArrayList<AllMenu> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.itemQuantities = new ArrayList<>();

        // Initialize itemQuantities with default values
        for (int i = 0; i < menuList.size(); i++) {
            itemQuantities.add(1);
        }
    }

    @NonNull
    @Override
    public AdminItemUploadDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemItemsBinding binding = ItemItemsBinding.inflate(inflater, parent, false);
        return new AdminItemUploadDeleteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemUploadDeleteViewHolder holder, int position) {
        holder.bind(position);

        holder.itemView.setOnClickListener(view -> {
            // Log to check if the click listener is triggered
            Log.d("ItemClick", "Item clicked at position: " + position);

            // Handle item click here (you can implement increase, decrease, or delete logic)
            // Example: Increase quantity by 1 when the item is clicked
            updateQuantities(position, itemQuantities.get(position) + 1);
        });

        holder.binding.PlusButton.setOnClickListener(view -> {
            // Log to check if the click listener is triggered
            Log.d("ButtonClick", "Plus button clicked at position: " + position);

            // Handle increase button click
            updateQuantities(position, itemQuantities.get(position) + 1);
        });

        holder.binding.minusButton.setOnClickListener(view -> {
            // Log to check if the click listener is triggered
            Log.d("ButtonClick", "Minus button clicked at position: " + position);

            // Handle decrease button click
            if (itemQuantities.get(position) > 1) {
                updateQuantities(position, itemQuantities.get(position) - 1);
            }
        });

        holder.binding.deleteButton.setOnClickListener(view -> {
            // Log to check if the click listener is triggered
            Log.d("ButtonClick", "Delete button clicked at position: " + position);

            // Handle delete button click
            removeItem(position);
        });
    }

    // Add a method to remove an item
    public void removeItem(int position) {
        if (position >= 0 && position < menuList.size()) {
            menuList.remove(position);
            itemQuantities.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Add a method to update the quantities if needed
    public void updateQuantities(int position, int quantity) {
        if (position >= 0 && position < itemQuantities.size()) {
            itemQuantities.set(position, quantity);

            // Notify the adapter that the data has changed
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class AdminItemUploadDeleteViewHolder extends RecyclerView.ViewHolder {

        private final ItemItemsBinding binding;

        public AdminItemUploadDeleteViewHolder(ItemItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            AllMenu menuItem = menuList.get(position);
            String imageUrl = menuItem.getFoodImage();

            // Assuming AllMenu has getters for name, price, and image
            binding.FoodNameTextView.setText(menuItem.getFoodName());
            binding.PriceTextView.setText(menuItem.getFoodPrice());

            // Use Glide to load the image
            Glide.with(context).load(imageUrl).into(binding.foodImageView);

            binding.QuantityTextView.setText(String.valueOf(itemQuantities.get(position)));

            // Add any additional binding logic here if needed
        }
    }
}
