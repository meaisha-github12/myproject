package com.example.jkk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.adapter.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycart_fragment, container, false);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList);
        recyclerView.setAdapter(cartAdapter);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve and display cart data
        fetchCartData();

        return view;
    }

    private void fetchCartData() {
        // Get the current user's UID
        String userId = auth.getCurrentUser().getUid();

        // Reference to the user's cart collection
        CollectionReference cartRef = db.collection("users").document(userId).collection("cart");

        // Fetch cart data
        cartRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Convert Firestore document to CartItem object
                CartItem cartItem = documentSnapshot.toObject(CartItem.class);
                cartItemList.add(cartItem);
            }

            // Notify the adapter that the data set has changed
            cartAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Handle failures
            Toast.makeText(getActivity(), "Failed to fetch cart data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
