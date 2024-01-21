package com.example.jkk;

import com.google.firebase.database.Exclude;

public class FoodItemModel {
    private String fileName;
    private String imageUrl;
    private String category;
    private String details;
    public FoodItemModel () {
        // Default constructor required for calls to DataSnapshot.getValue(Upload.class)
    }

    public FoodItemModel (String fileName, String imageUrl) {
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
    public void setCategory(String imageUrl) {
        this.category = category;
    }
    public void setDetails(String Details){
        this.details =details;
    }


}
