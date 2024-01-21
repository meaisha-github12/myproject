package com.example.jkk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.R;
import com.example.jkk.Upload;
import com.example.jkk.ui.home.UserAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class VouchersFragment extends Fragment {

    private ProgressBar mProgressCircle;
    private RecyclerView mRecyclerView;
    private UserVoucher mAdapter;
    private FirebaseFirestore db;
    private CollectionReference uploadsCollection;
    private ListenerRegistration uploadListener;

    private List<Upload> mUploads;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.vouchers_fragment, container, false);

        mRecyclerView = root.findViewById(R.id.recycler_view1);
        mProgressCircle = root.findViewById(R.id.progress_circle1);

        // Initialize RecyclerView and Adapter
        mUploads = new ArrayList<>();
        mAdapter = new UserVoucher(getContext(),mUploads );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(mAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        uploadsCollection = db.collection("vouchers");

        // Set up a Firestore snapshot listener
        uploadListener = uploadsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("FirestoreError", "Error getting documents: ", e);
                // Handle errors or provide user feedback
                return;
            }


            mUploads.clear();
            if (queryDocumentSnapshots != null) {
                // Log.d("fire", queryDocumentSnapshots.getMetadata() ? "load" : "");
                mUploads.addAll(queryDocumentSnapshots.toObjects(Upload.class));
            }


            mAdapter.notifyDataSetChanged();
            mProgressCircle.setVisibility(View.INVISIBLE);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the Firestore snapshot listener when the Fragment is destroyed
        if (uploadListener != null) {
            uploadListener.remove();
        }
    }
}