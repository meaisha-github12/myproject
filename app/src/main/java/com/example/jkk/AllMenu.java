// AllMenu.java
package com.example.jkk;

import android.os.Parcel;
import android.os.Parcelable;

public class AllMenu implements Parcelable {
    private String foodName;
    private String fileName;
    private String foodPrice;
    private String foodDescription;
    private String foodIngredient;
    private String foodImage;

    public AllMenu() {
        // Default constructor required for Firestore
    }

    public AllMenu(String foodName, String fileName, String foodPrice, String foodDescription, String foodIngredient, String foodImage) {
        this.foodName = foodName;
        this.fileName = fileName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodIngredient = foodIngredient;
        this.foodImage = foodImage;
    }

    protected AllMenu(Parcel in) {
        foodName = in.readString();
        fileName = in.readString();
        foodPrice = in.readString();
        foodDescription = in.readString();
        foodIngredient = in.readString();
        foodImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodName);
        dest.writeString(fileName);
        dest.writeString(foodPrice);
        dest.writeString(foodDescription);
        dest.writeString(foodIngredient);
        dest.writeString(foodImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllMenu> CREATOR = new Creator<AllMenu>() {
        @Override
        public AllMenu createFromParcel(Parcel in) {
            return new AllMenu(in);
        }

        @Override
        public AllMenu[] newArray(int size) {
            return new AllMenu[size];
        }
    };

    public String getFoodName() {
        return foodName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodIngredient() {
        return foodIngredient;
    }

    public String getFoodImage() {
        return foodImage;
    }
}
