package com.example.jkk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminhelpCenter extends AppCompatActivity {

    private EditText etNewContent;
    private static final String TAG = "AdminPanelActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhelp_center);

        etNewContent = findViewById(R.id.etNewContent);
        Button btnAddContent = findViewById(R.id.btnAddContent);

        btnAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContent();
            }
        });
    }

    private void addNewContent() {
        String newContent = etNewContent.getText().toString().trim();

        if (!newContent.isEmpty()) {
            // Replace "helpCenter" with your actual Firestore collection name
            Map<String, Object> data = new HashMap<>();
            data.put("content", newContent);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Add data to Firestore
            db.collection("helpCenter")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        etNewContent.setText("");
                        // Add any success feedback if needed
                        Toast.makeText(AdminhelpCenter.this, "Content added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e(TAG, "Error adding document", e);
                        Toast.makeText(AdminhelpCenter.this, "Failed to add content", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
