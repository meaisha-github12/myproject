package com.example.jkk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomepg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepg);

    }
    public void upload(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminUpload.class));
    }
    public void logout(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminLogout.class));
    }
    public void  itemdelete(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminItemUploadDelete.class));
    }
    public void   Vouchers(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminVoucher.class));
    }
    public void   AllItem(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminAllItemActivity.class));
    }
    public void   setting(View view) {
        startActivity(new Intent(AdminHomepg.this, AdminSetting.class));
    }
    public void POPUP(View view) {
        // Intent to navigate to PopupAdminActivity
        Intent intent = new Intent(AdminHomepg.this, popupAdminactivity.class);
        startActivity(intent);
    }

}
