package com.example.jkk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HelpCenterFragment extends Fragment {

    private static final String TAG = "HelpCenterFragment";
    private ListView listView;
    private List<String> contentList;
    private HelpcenterAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.helpcenter_fragment, container, false);

        listView = view.findViewById(R.id.tvHelpCenterContent);
        contentList = new ArrayList<>();
        adapter = new HelpcenterAdapter(requireActivity().getApplicationContext(), R.layout.list_helpcenter_content, contentList);
        listView.setAdapter(adapter);

        retrieveContentFromFirestore();

        return view;
    }

    private void retrieveContentFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Replace "helpCenter" with your actual Firestore collection name
        db.collection("helpCenter")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Assuming your data field is named "content"
                            String content = documentSnapshot.getString("content");
                            contentList.add(content);
                        }

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting documents", e);
                        Toast.makeText(requireContext(), "Failed to retrieve content", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
