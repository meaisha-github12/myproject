package com.example.jkk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);
    }
   public void   Setting(View view) {
      startActivity(new Intent(AdminSetting.this, AdminChangeSetting.class));
  }
    public void   helpCenter(View view) {
        startActivity(new Intent(AdminSetting.this, AdminhelpCenter.class));
    }
}