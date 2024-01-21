package com.example.jkk;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.adapter.AddItemAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class AdminAllItemActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemsRef = db.collection("items");
    private ArrayList<AllMenu> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_item);

        // Initialize views
        ImageButton goBackButton = findViewById(R.id.goBackbutton2);
        TextView allItemsTextView = findViewById(R.id.allitems);

        // Add click listener to go back button
        goBackButton.setOnClickListener(v -> finish()); // Finish the activity when the button is clicked

        // Retrieve menu items from Firestore
        retrieveMenuItem();
    }

    private void retrieveMenuItem() {
        itemsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    menuItems.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        AllMenu menuItem = document.toObject(AllMenu.class);
                        menuItems.add(menuItem);
                    }
                    setAdapter();
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error getting documents.", e);
                });
    }

    private void setAdapter() {
        AddItemAdapter adapter = new AddItemAdapter(this, menuItems);
        RecyclerView menuRecyclerView = findViewById(R.id.MenueRecyclerview);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setAdapter(adapter);
    }
}
