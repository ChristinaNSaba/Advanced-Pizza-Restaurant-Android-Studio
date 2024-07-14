package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PizzaFragmentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_view_fragment);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pizza_name")) {
            String pizzaName = intent.getStringExtra("pizza_name");
            double pizzaPrice = intent.getDoubleExtra("pizza_price", 0);
            String pizzaDescription = intent.getStringExtra("pizza_description");
            String pizzaImage = intent.getStringExtra("pizza_image");

            PizzaInfoFragment pizzaInfoFragment = PizzaInfoFragment.newInstance(
                    pizzaName,
                    pizzaPrice,
                    pizzaDescription,
                    pizzaImage
            );
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, pizzaInfoFragment)
                    .commit();
        }
    }
}