package com.example.restaurantlitalianopizza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewPizzasSave {
    private static NewPizzasSave instance;
    private ArrayList<String> pizzaTypes = new ArrayList<>();
    private ArrayList<Pizza> pizzaArrayList = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Pizza> favorites = new ArrayList<>();
    private ArrayList<Order> AllOrders = new ArrayList<>();
    private HashMap<String, String> User = new HashMap<>();
    private ArrayList<Pizza> viggies = new ArrayList<>();
    private ArrayList<Pizza> beef = new ArrayList<>();
    private ArrayList<Pizza> chicken = new ArrayList<>();
    private ArrayList<Pizza> seafood = new ArrayList<>();
    private ArrayList<Pizza> price499 = new ArrayList<>();
    private ArrayList<Pizza> price999 = new ArrayList<>();
    private ArrayList<Pizza> price1499 = new ArrayList<>();
    private ArrayList<Pizza> price1999 = new ArrayList<>();



    private ArrayList<SpecialOffer> specialOffers = new ArrayList<>();


    private NewPizzasSave() {
    }

    public static synchronized NewPizzasSave getInstance() {
        if (instance == null) {
            instance = new NewPizzasSave();
        }
        return instance;
    }

    public ArrayList<Pizza> getPizzaArrayList() {
        return pizzaArrayList;
    }
    public ArrayList<Pizza> getViggies() {
        return viggies;
    }

    public void setViggies(ArrayList<Pizza> viggies) {
        this.viggies = viggies;
    }

    public ArrayList<Pizza> getBeef() {
        return beef;
    }

    public void setBeef(ArrayList<Pizza> beef) {
        this.beef = beef;
    }

    public ArrayList<Pizza> getChicken() {
        return chicken;
    }

    public void setChicken(ArrayList<Pizza> chicken) {
        this.chicken = chicken;
    }

    public ArrayList<Pizza> getSeafood() {
        return seafood;
    }

    public void setSeafood(ArrayList<Pizza> seafood) {
        this.seafood = seafood;
    }

    public ArrayList<Pizza> getPrice499() {
        return price499;
    }

    public void setPrice499(ArrayList<Pizza> price499) {
        this.price499 = price499;
    }

    public ArrayList<Pizza> getPrice999() {
        return price999;
    }

    public void setPrice999(ArrayList<Pizza> price999) {
        this.price999 = price999;
    }

    public ArrayList<Pizza> getPrice1499() {
        return price1499;
    }

    public void setPrice1499(ArrayList<Pizza> price1499) {
        this.price1499 = price1499;
    }

    public ArrayList<Pizza> getPrice1999() {
        return price1999;
    }

    public void setPrice1999(ArrayList<Pizza> price1999) {
        this.price1999 = price1999;
    }

    public void setPizzaArrayList(ArrayList<Pizza> pizzaArrayList) {
        this.pizzaArrayList = pizzaArrayList;
    }
    public ArrayList<String> getPizzaTypes() {
        return pizzaTypes;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    public ArrayList<Order> getAllOrders() {
        return AllOrders;
    }


    public ArrayList<Pizza> getFavorites() {
        return favorites;
    }

    public void setPizzaTypes(ArrayList<String> pizzaTypes) {
        this.pizzaTypes = pizzaTypes;
    }

    public void addViggies(Pizza pizza) {
        if (!viggies.contains(pizza)) {
            viggies.add(pizza);
        }
    }
    public void addChicken(Pizza pizza) {
        if (!chicken.contains(pizza)) {
            chicken.add(pizza);
        }
    }
    public void addBeef(Pizza pizza) {
        if (!beef.contains(pizza)) {
            beef.add(pizza);
        }
    }
    public void addSeafood(Pizza pizza) {
        if (!seafood.contains(pizza)) {
            seafood.add(pizza);
        }
    }
    public void addPrice499(Pizza pizza) {
        if (!price499.contains(pizza)) {
            price499.add(pizza);
        }
    }
    public void addPrice999(Pizza pizza) {
        if (!price999.contains(pizza)) {
            price999.add(pizza);
        }
    }
    public void addPrice1499(Pizza pizza) {
        if (!price1499.contains(pizza)) {
            price1499.add(pizza);
        }
    }

    public void addPrice1999(Pizza pizza) {
        if (!price1999.contains(pizza)) {
            price1999.add(pizza);
        }
    }
    public void addFavoritePizza(Pizza pizza) {
        if (!favorites.contains(pizza)) {
            favorites.add(pizza);
        }
    }

    public void addOrderPizza(Order pizza) {
        if (!orders.contains(pizza)) {
            orders.add(pizza);
        }
    }
    public void addAllOrders(Order pizza) {
        if (!AllOrders.contains(pizza)) {
            AllOrders.add(pizza);
        }
    }
    public String getUsername(String key){
        return this.User.get(key);
    }

    public void AddUser(String key, String value){
        this.User.put(key, value);
    }
    public void removeOrderPizza(Pizza pizza) {
        orders.remove(pizza);
    }
    public void removeFromFavorites(Pizza pizza){favorites.remove(pizza);}
    public void addSpecialOffer(SpecialOffer specialOffer) {
        if (!specialOffers.contains(specialOffer)) {
            specialOffers.add(specialOffer);
        };
    }

    public ArrayList<SpecialOffer> getSpecialOffers() {
        return specialOffers;
    }
}
