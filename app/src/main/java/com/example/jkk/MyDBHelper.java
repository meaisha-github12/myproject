package com.example.jkk;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "register.db";

    public MyDBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table allusers (full_name TEXT primary key, password TEXT, email_address TEXT unique)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists allusers");
        onCreate(db);
    }

    public Boolean insertData(String full_name, String password, String email_address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", full_name);
        contentValues.put("password", password);
        contentValues.put("email_address", email_address);
        try {
            long result = db.insert("allusers", null, contentValues);
            db.close(); // Close the database connection
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean checkFull_name(String full_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allusers WHERE full_name = ?", new String[]{full_name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public Boolean checkFull_namePasswordEmail_address(String full_name, String email_address, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allusers WHERE full_name = ? AND email_address = ? AND password = ?",
                new String[]{full_name, email_address, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public boolean checkLoginCredentials(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM allusers WHERE email_address = ? AND password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            Log.d("Login", "Login successful");
            return true;
        } else {
            Log.d("Login", "Login failed");
            return false;
        }
    }
}
