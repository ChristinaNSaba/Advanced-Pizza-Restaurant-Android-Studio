package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AdminAddSpecialOffersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner spinnerPizzaType;
    private Spinner spinnerPizzaSize;
    private EditText editTextOfferPeriod;
    private EditText editTextTotalPrice;
    private Button buttonAddOffer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView imageView;

    private String[] pizzaNames = {
            "Margarita",
            "Neapolitan",
            "Hawaiian",
            "Pepperoni",
            "NewYorkStyle",
            "Calzone",
            "TandooriChicken",
            "BBQChicken",
            "Seafood",
            "Vegetarian",
            "Buffalo",
            "MushroomTruffle",
            "Pesto Chicken"
    };

    private String[] pizzaSizes = {"Small", "Medium", "Large"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_special_offers);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        spinnerPizzaType = findViewById(R.id.spinnerPizzaType);
        spinnerPizzaSize = findViewById(R.id.spinnerPizzaSize);
        editTextOfferPeriod = findViewById(R.id.editTextOfferPeriod);
        editTextTotalPrice = findViewById(R.id.editTextTotalPrice);
        buttonAddOffer = findViewById(R.id.buttonAddOffer);
        imageView = (ImageView)findViewById(R.id.imageView);

        ArrayAdapter<String> pizzaTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaNames);
        pizzaTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaType.setAdapter(pizzaTypeAdapter);

        ArrayAdapter<String> pizzaSizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaSizes);
        pizzaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaSize.setAdapter(pizzaSizeAdapter);

        buttonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpecialOffer();
            }
        });
    }

    private void addSpecialOffer() {
        String pizzaType = spinnerPizzaType.getSelectedItem().toString();
        String pizzaSize = spinnerPizzaSize.getSelectedItem().toString();
        String offerPeriod = editTextOfferPeriod.getText().toString().trim();
        String totalPrice = editTextTotalPrice.getText().toString().trim();

        if (TextUtils.isEmpty(pizzaType) || TextUtils.isEmpty(pizzaSize) ||
                TextUtils.isEmpty(offerPeriod) || TextUtils.isEmpty(totalPrice)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(totalPrice);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        SpecialOffer specialOffer = new SpecialOffer(pizzaType, pizzaSize, offerPeriod, price);
        NewPizzasSave.getInstance().addSpecialOffer(specialOffer);

        Toast.makeText(this, "Special Offer Added Successfully", Toast.LENGTH_SHORT).show();
        imageView.startAnimation(AnimationUtils.loadAnimation(AdminAddSpecialOffersActivity.this, R.anim.rotate));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.adminprofile) {
            Intent welcomeIntent = new Intent(this, AdminProfileActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.addadmin) {
            Intent menu = new Intent(this, AddAdminActivity.class);
            startActivity(menu);
        } else if (id == R.id.addspecialoffers) {
            Intent favorites = new Intent(this, AdminAddSpecialOffersActivity.class);
            startActivity(favorites);
        } else if (id == R.id.viewallorders) {
            Intent orders = new Intent(this, AdminViewAllOrderActivity.class);
            startActivity(orders);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(this, LoginActivity.class);
            startActivity(logout);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}
