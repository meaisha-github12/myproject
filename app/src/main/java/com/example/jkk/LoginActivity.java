package com.example.jkk;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jkk.AdminHomepg;
import com.example.jkk.ForgotPassword;
import com.example.jkk.NavigationActivity;
import com.example.jkk.R;
import com.example.jkk.RegisterationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        fAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
        finish();
    }

    public void ForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
        finish();
    }

    public void openNavigationDrawer(View view) {
        EditText emailEditText = findViewById(R.id.editText2);
        EditText passwordEditText = findViewById(R.id.editText);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStrongPassword(password)) {
            Toast.makeText(LoginActivity.this, "Password must be at least 8 characters and contain both uppercase, lowercase, special character, and numeric values, without spaces", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login using Firebase Authentication
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful, retrieve user data from Firestore
                        FirebaseUser user = fAuth.getCurrentUser();
                        if (user != null) {
                            retrieveUserData(user.getUid());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(String email) {
        // Use regex for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check if the email contains spaces
        return !email.contains(" ") && email.matches(emailPattern);
    }

    private boolean isStrongPassword(String password) {
        // Use regex for password strength validation
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        // Check if the password contains spaces
        return !password.contains(" ") && password.matches(passwordPattern);
    }

    private void retrieveUserData(String userId) {
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User data retrieved successfully
                            String name = document.getString("name");
                            String email = document.getString("email");
                            String role = document.getString("role");

                            // Store user data in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("your_preference_name", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("role", role);
                            editor.apply();

                            Log.d("role", role);
                            if (role.contains("admin")) {
                                Intent mainActivity = new Intent(LoginActivity.this, AdminHomepg.class);
                                startActivity(mainActivity);
                            } else {
                                // Navigate to the main activity or any other desired activity
                                Intent mainActivity = new Intent(LoginActivity.this, NavigationActivity.class);
                                startActivity(mainActivity);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
