package com.example.jkk.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.AllMenu;
import com.example.jkk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<AllMenu> menuList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Context context;

    public MenuAdapter(List<AllMenu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllMenu menu = menuList.get(position);
        String imageUrl = menu.getFoodImage();
        holder.PriceTextView.setText(menu.getFoodPrice());
        holder.foodNameTextView.setText(menu.getFoodName());
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.foodImageView);
        }
        holder.quantity.setText("0");

        holder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.quantity.setText(String.valueOf(quantity));
                }
            }
        });

        holder.btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity++;
                holder.quantity.setText(String.valueOf(quantity));
            }
        });

        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to add to the cart
                addToCart(menu, holder.quantity.getText().toString());
            }
        });
    }

    private void addToCart(AllMenu menu, String quantity) {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // Convert quantity to integer
        int intQuantity = Integer.parseInt(quantity);

        // Calculate total price based on quantity and individual item price
        double itemPrice = Double.parseDouble(menu.getFoodPrice());
        double totalPrice = intQuantity * itemPrice;

        // Create a HashMap to store cart information
        Map<String, Object> cartMap = new HashMap<>();
        cartMap.put("FoodName", menu.getFoodName());
        cartMap.put("FoodPrice", menu.getFoodPrice());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity);
        cartMap.put("totalPrice", String.valueOf(totalPrice)); // Convert totalPrice to String

        // Get the current user's UID
        String userId = auth.getCurrentUser().getUid();

        // Add the cartMap to Firestore under the user's document
        db.collection("users")
                .document(userId)
                .collection("cart")
                .add(cartMap)
                .addOnSuccessListener(documentReference -> {
                    // Show a toast message
                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Toast.makeText(context, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        ImageView foodImageView;
        TextView PriceTextView;
        Button btnplus;
        Button btnminus;
        Button addtocart;
        TextView quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.FoodNameTextView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            PriceTextView = itemView.findViewById(R.id.PriceTextView);
            btnminus = itemView.findViewById(R.id.minusButton);
            btnplus = itemView.findViewById(R.id.PlusButton);
            addtocart = itemView.findViewById(R.id.addcartButton);
            quantity = itemView.findViewById(R.id.QuantityTextView);
        }
    }
}
