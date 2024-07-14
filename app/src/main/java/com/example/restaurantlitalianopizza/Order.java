package com.example.restaurantlitalianopizza;

public class Order {

    private String name;
    private String category;
    private double price;
    private String image;
    private String type;
    private String size;
    private String date;
    private int quatity;
    private String Username;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quatity;
    }

    public void setQuantity(int quatity) {
        this.quatity = quatity;
    }

    public Order(String name, String category, double price, String image, String type, String size, String date, int quantity, String Username) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.type = type;
        this.size = size;
        this.date = date;
        this.quatity = quantity;
        this.Username = Username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

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

}
