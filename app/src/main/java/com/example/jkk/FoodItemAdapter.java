package com.example.jkk;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jkk.databinding.FoodItemCardBinding;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

        private final List<String> items;
        private final List<String> price;
        private final List<Integer> image;

        public FoodItemAdapter(List<String> items, List<String> price, List<Integer> image) {
                this.items = items;
                this.price = price;
                this.image = image;
        }

        @Override
        public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                FoodItemCardBinding binding = FoodItemCardBinding.inflate(inflater, parent, false);
                return new FoodItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(FoodItemViewHolder holder, int position) {
                String item = items.get(position);
                String itemPrice = price.get(position);
                int itemImage = image.get(position);
                holder.bind(item, itemPrice, itemImage);
        }

        @Override
        public int getItemCount() {
                return items.size();
        }

        public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
                private final FoodItemCardBinding binding;

                public FoodItemViewHolder(FoodItemCardBinding binding) {
                        super(binding.getRoot());
                        this.binding = binding;
                }

                public void bind(String item, String price, int image) {
                        binding.FoodNameTextView.setText(item);
                        binding.PriceTextView.setText(price);
                        binding.foodImageView.setImageResource(image);
                }
        }
}
