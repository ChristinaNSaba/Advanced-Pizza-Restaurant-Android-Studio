package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompleteOrderActivity extends AppCompatActivity {

    private TextView pizzaNameTextView, pizzaPriceTextView, totalPriceTextView;
    private Spinner sizeSpinner, quantitySpinner, toppingsSpinner;
    private Button submitButton;
    private ImageView pizzaImageTextView;
    private Pizza orderedPizza;
    private double basePrice;
    private double totalAmount;
    private double sizeFactor;
    private int quantity;
    private String pizzasize;
    private double toppingsPrice;

    private boolean fromSpecialOffers = false; // Flag to check if intent is from SpecialOffersActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        // Initialize views
        pizzaImageTextView = findViewById(R.id.pizzaImageView);
        pizzaNameTextView = findViewById(R.id.pizzaNameTextView);
        pizzaPriceTextView = findViewById(R.id.pizzaPriceTextView);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        quantitySpinner = findViewById(R.id.quantitySpinner);
        toppingsSpinner = findViewById(R.id.toppingsSpinner);
        submitButton = findViewById(R.id.submitButton);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        // Get pizza object from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ordered_pizza")) {
            orderedPizza = intent.getParcelableExtra("ordered_pizza");
            if (orderedPizza != null) {
                basePrice = orderedPizza.getPrice();
                displayPizzaDetails();
                // Check if intent is from SpecialOffersActivity
                if (intent.hasExtra("from_special_offers")) {
                    fromSpecialOffers = intent.getBooleanExtra("from_special_offers", false);
                    if (fromSpecialOffers) {
                        // Set size spinner based on the pizza offer from SpecialOffersActivity
                        pizzasize = intent.getStringExtra("pizza_size");
                        setSizeSpinner();
                    }
                }
            }
        }

        // Setup size spinner
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_options, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Setup quantity spinner
        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(this,
                R.array.quantity_options, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);

        // Setup toppings spinner
        ArrayAdapter<CharSequence> toppingsAdapter = ArrayAdapter.createFromResource(this,
                R.array.toppings_options, android.R.layout.simple_spinner_item);
        toppingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toppingsSpinner.setAdapter(toppingsAdapter);

        // Calculate total price when spinners' selections change
        sizeSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        quantitySpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        toppingsSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);

        // Submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrders();
            }
        });

        // Set initial total price
        calculateTotalPrice();
    }

    // Listener for spinners' item selection
    private AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            calculateTotalPrice();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    };

    // Display pizza details
    private void displayPizzaDetails() {
        if (orderedPizza != null) {
            pizzaNameTextView.setText(orderedPizza.getName());
            pizzaPriceTextView.setText(String.format("$%.2f", basePrice));
            String pizzaImage = orderedPizza.getImage();
            if (pizzaImage != null && !pizzaImage.isEmpty()) {
                int imageResource = getResources().getIdentifier(pizzaImage, "drawable", getPackageName());
                if (imageResource != 0) {
                    pizzaImageTextView.setImageResource(imageResource);
                }
            }
        }
    }
    private void setSizeSpinner() {
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_options, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Set selected size based on the pizza offer from SpecialOffersActivity
        if (pizzasize != null) {
            int position = sizeAdapter.getPosition(pizzasize);
            sizeSpinner.setSelection(position);
        }

        // Disable spinner interaction
        sizeSpinner.setEnabled(false);
        sizeSpinner.setClickable(false);
        sizeSpinner.setFocusable(false);
    }

    private void calculateTotalPrice() {
        sizeFactor = getSizeFactor(sizeSpinner.getSelectedItem().toString());
        toppingsPrice = getToppingsPrice(toppingsSpinner.getSelectedItem().toString());
        quantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());

        totalAmount = (basePrice * sizeFactor + toppingsPrice) * quantity;
        totalPriceTextView.setText(String.format("$%.2f", totalAmount));
    }

    private double getSizeFactor(String size) {
        switch (size) {
            case "Medium":
                pizzasize = "Medium";
                return 2.0;
            case "Large":
                pizzasize = "Large";
                return 3.5;
            default:
                pizzasize = "Small";
                return 1.0;
        }
    }

    private double getToppingsPrice(String toppings) {
        switch (toppings) {
            case "Extra Cheese":
                return 1.5;
            case "Mushrooms":
                return 2.0;
            case "Pepperoni":
                return 2.5;
            default:
                return 0.0;
        }
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // Add ordered pizza to the list
    private void addToOrders() {
        orderedPizza.setPrice(totalAmount);
        String username = NewPizzasSave.getInstance().getUsername("name");
        String currentDate = getCurrentDate();
        Order order = new Order(orderedPizza.getName(), orderedPizza.getCategory(), orderedPizza.getPrice(), orderedPizza.getImage(), orderedPizza.getType(), pizzasize, currentDate, quantity, username);
        NewPizzasSave.getInstance().addOrderPizza(order);
        NewPizzasSave.getInstance().addAllOrders(order);
        Toast.makeText(this, "Pizza added to orders", Toast.LENGTH_SHORT).show();
        Intent orderIntent = new Intent(CompleteOrderActivity.this, OrdersActivity.class);
        startActivity(orderIntent);
        finish();
    }
}
