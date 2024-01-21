package com.example.jkk;

import com.google.firebase.database.Exclude;

public class Upload {
    private String fileName;
    private String imageUrl;
    private String mKey;
    public Upload() {
        // Default constructor required for calls to DataSnapshot.getValue(Upload.class)
    }

    public Upload(String fileName, String imageUrl) {
        if (fileName == null || fileName.trim().isEmpty()) {
            this.fileName = "No Name";
        } else {
            this.fileName = fileName.trim();
        }

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            this.imageUrl = "No Image URL";
        } else {
            this.imageUrl = imageUrl.trim();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return mKey;
    }


    public void setKey(String key) {
        mKey = key;
    }
}
