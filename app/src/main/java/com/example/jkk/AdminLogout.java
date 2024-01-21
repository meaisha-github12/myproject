package com.example.jkk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLogout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logout);

        // Find the "Logout" button in the layout
        Button logoutButton = findViewById(R.id.adm_btn_logout);

        // Set a click listener on the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before logging out
                showLogoutConfirmationDialog();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove user preferences
                removeUserPreferences();

                // Show the email from preferences in a toast message
                showEmailFromPreferences();

                // Navigate to HomeActivity
                Intent intent = new Intent(AdminLogout.this, RegisterationActivity.class);
                startActivity(intent);

                // Finish the current activity to prevent the user from coming back with the back button
                finish();
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
        SharedPreferences sharedPreferences = getSharedPreferences("your_preference_name", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("name");
        editor.remove("role");
        editor.apply();
    }

    private void showEmailFromPreferences() {
        // Retrieve the email from preferences
        SharedPreferences sharedPreferences = getSharedPreferences("your_preference_name", 0);
        String userEmail = sharedPreferences.getString("email", "");

        // Show the email in a toast message
        Toast.makeText(AdminLogout.this, "Logged out. Email: " + userEmail, Toast.LENGTH_SHORT).show();
    }
}
