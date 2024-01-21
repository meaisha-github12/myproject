package com.example.jkk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.adapter.MenuAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserMenu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<AllMenu> menuList;
    String receivedText;  // Move this here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        // Move this line here to get the value in the onCreate method
        receivedText = getIntent().getStringExtra("category");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize menuList
        menuList = new ArrayList<>();

        // Initialize MenuAdapter
        menuAdapter = new MenuAdapter(menuList, this);
        recyclerView.setAdapter(menuAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").whereEqualTo("fileName", receivedText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert Firestore document to MenuItems object
                                MenuItems menuItems = document.toObject(MenuItems.class);

                                // Create AllMenu object from MenuItems and add to menuList
                                AllMenu allMenu = new AllMenu(
                                        menuItems.getFoodName(),
                                        menuItems.getFileName(),
                                        menuItems.getFoodPrice(),
                                        menuItems.getFoodDescription(),
                                        menuItems.getFoodIngredient(),
                                        menuItems.getFoodImage()
                                );

                                menuList.add(allMenu);
                            }

                            // Notify the adapter that the data set has changed
                            menuAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                });
    }
}
