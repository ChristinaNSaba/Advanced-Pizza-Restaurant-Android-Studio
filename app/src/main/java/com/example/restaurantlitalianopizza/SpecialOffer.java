package com.example.restaurantlitalianopizza;

public class SpecialOffer {
    private String pizzaType;
    private String pizzaSize;

    private String category;
    private String offerPeriod;
    private double totalPrice;
    private String image;


    public SpecialOffer(String pizzaType, String pizzaSize, String offerPeriod, double totalPrice) {
        this.pizzaType = pizzaType;
        this.pizzaSize = pizzaSize;
        this.offerPeriod = offerPeriod;
        this.totalPrice = totalPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public String getOfferPeriod() {
        return offerPeriod;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
