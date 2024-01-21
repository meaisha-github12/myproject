package com.example.jkk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jkk.databinding.ActivityAdminItemUploadDeleteBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminItemUploadDelete extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityAdminItemUploadDeleteBinding binding;
    private FirebaseFirestore firestore;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminItemUploadDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();

        // Check if the ActionBar is not null before hiding
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up the Spinner with dynamically fetched file names
        setupFileNameSpinner();

        // Set up click listener for "Select Image" TextView
        binding.selectImage.setOnClickListener(v -> {
            // Launch gallery intent
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        });

        // Set up click listener for "Add items" button
        binding.uploadButton.setOnClickListener(view -> {
            // Retrieve values from input fields
            String foodName = binding.enterFoodName.getText().toString().trim();
            String fileName = binding.fileNameSpinner.getSelectedItem().toString();
            String price = binding.enterPrice.getText().toString().trim();
            String description = binding.description.getText().toString().trim();
            String ingredient = binding.ingredient.getText().toString().trim();

            // Check if any of the fields is empty
            if (foodName.isEmpty() || fileName.equals("Select food category") || price.isEmpty() || description.isEmpty() || ingredient.isEmpty()) {
                Toast.makeText(AdminItemUploadDelete.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Upload the image to Firebase Storage and save the item to Firestore
            uploadImageAndSaveItem(foodName, fileName, price, description, ingredient);
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                binding.selectedImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageAndSaveItem(String foodName, String fileName, String price, String description, String ingredient) {
        if (selectedImageUri != null) {
            try {
                // Convert image to byte array
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                // Create a reference to the storage path where you want to save the image
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("items").child(System.currentTimeMillis() + ".jpg");

                // Upload the file to Firebase Storage
                UploadTask uploadTask = storageRef.putBytes(imageData);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL to Firestore
                        saveItemToFirestore(foodName, fileName, price, description, ingredient, uri.toString());
                    });
                }).addOnFailureListener(e -> {
                    // Handle the failure to upload the image
                    Log.e("Firebase Storage", "Error uploading image: " + e.getMessage());
                    Toast.makeText(AdminItemUploadDelete.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AdminItemUploadDelete.this, "Error converting image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveItemToFirestore(String foodName, String fileName, String price, String description, String ingredient, String imageUrl) {
        // Create a new document in Firestore
        firestore.collection("items")
                .add(new AllMenu(foodName, fileName, price, description, ingredient, imageUrl))
                .addOnSuccessListener(documentReference -> {
                    // Clear input fields
                    binding.enterFoodName.setText("");
                    binding.fileNameSpinner.setSelection(0);
                    binding.enterPrice.setText("");
                    binding.description.setText("");
                    binding.ingredient.setText("");

                    // Log success and document ID
                    Log.d("Firestore", "Document added with ID: " + documentReference.getId());

                    // Show a success message
                    Toast.makeText(AdminItemUploadDelete.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Log the error
                    Log.e("Firestore", "Error adding item: " + e.getMessage());

                    // Show an error message
                    Toast.makeText(AdminItemUploadDelete.this, "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupFileNameSpinner() {
        // Create an empty list to store dynamically fetched file names
        List<String> fileNames = new ArrayList<>();

        // Create an ArrayAdapter using a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fileNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.fileNameSpinner.setAdapter(adapter);

        // Set a listener to handle item selections
        binding.fileNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected file name
                String selectedFileName = fileNames.get(position);
                // Do something with the selected file name if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Fetch file names from Firestore "uploads" collection
        firestore.collection("uploads")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the list before adding Firestore data
                        fileNames.clear();

                        // Add a hint to the list
                        fileNames.add("Select food category");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming you have a field named "fileName" in your Firestore documents
                            String fileName = document.getString("fileName");

                            // Check for null before adding to the list
                            if (fileName != null) {
                                // Add the fetched file name to the list
                                fileNames.add(fileName);
                            }
                        }
                        // Notify the adapter that data set changed
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle the failure to fetch data
                        Log.e("Firestore", "Error fetching data: " + task.getException());
                        Toast.makeText(AdminItemUploadDelete.this, "Error fetching data: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
