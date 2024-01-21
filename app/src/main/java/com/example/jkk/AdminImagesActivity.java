package com.example.jkk;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class AdminImagesActivity extends AppCompatActivity implements AdminImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private AdminImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseFirestore db;
    private CollectionReference uploadsCollection;

    private FirebaseStorage mStorage;

    private List<Upload> mUploads;

    // Declare ListenerRegistration for Firestore listener
    private ListenerRegistration mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new AdminImageAdapter(AdminImagesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(AdminImagesActivity.this);

        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        uploadsCollection = db.collection("uploads");

        // Assign the listener registration when setting up the Firestore listener
        mDBListener = uploadsCollection.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(AdminImagesActivity.this, "Error getting documents: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
                return;
            }

            if (value != null) {
                mUploads.clear();

                for (QueryDocumentSnapshot document : value) {
                    Upload upload = document.toObject(Upload.class);
                    upload.setKey(document.getId());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                uploadsCollection.document(selectedKey).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminImagesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener when the activity is destroyed
        if (mDBListener != null) {
            mDBListener.remove();
        }
    }
}
