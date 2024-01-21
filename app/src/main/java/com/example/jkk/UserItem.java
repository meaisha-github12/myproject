package com.example.jkk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserItem extends AppCompatActivity {
    private Button Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item);

        // Find the button by ID
        Menu = findViewById(R.id.Menu);
    }

    public void move(View view) {
        startActivity(new Intent(UserItem.this, UserMenu.class));
    }
}
