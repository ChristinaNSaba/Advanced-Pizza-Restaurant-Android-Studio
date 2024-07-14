package com.example.restaurantlitalianopizza;

import android.os.Parcel;
import android.os.Parcelable;

public class Pizza implements Parcelable {

    private String name;
    private String category;
    private double price;
    private String image;
    private String type;

    public Pizza(String name, String category, double price, String image, String type) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.type = type;
    }

    protected Pizza(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        image = in.readString();
        type = in.readString();
    }

    public static final Creator<Pizza> CREATOR = new Creator<Pizza>() {
        @Override
        public Pizza createFromParcel(Parcel in) {
            return new Pizza(in);
        }

        @Override
        public Pizza[] newArray(int size) {
            return new Pizza[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Pizza: " + name + ", Category: " + category + ", Price: $" + price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeDouble(price);
        dest.writeString(image);
        dest.writeString(type);
    }
}
