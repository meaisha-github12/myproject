package com.example.jkk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_fragment, container, false);

        // Find the "Logout" button in the layout
        Button logoutButton = view.findViewById(R.id.btn_logout);

        // Set a click listener on the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before logging out
                showLogoutConfirmationDialog();
            }
        });

        return view;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove user preferences
                removeUserPreferences();

                // Show the email from preferences in a toast message
                showEmailFromPreferences();

                // Navigate to RegistrationActivity
                Intent intent = new Intent(requireContext(), RegisterationActivity.class);
                startActivity(intent);

                // Finish the current activity to prevent the user from coming back with the back button
                requireActivity().finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the logout
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeUserPreferences() {
        // Remove user preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("your_preference_name", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("name");
//        editor.remove("email");
        editor.remove("role");
        editor.apply();
    }

    private void showEmailFromPreferences() {
        // Retrieve the email from preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("your_preference_name", 0);
        String userEmail = sharedPreferences.getString("email","");

        // Show the email in a toast message
        Toast.makeText(requireContext(), "Logged out. Email: " + userEmail, Toast.LENGTH_SHORT).show();
    }
}
