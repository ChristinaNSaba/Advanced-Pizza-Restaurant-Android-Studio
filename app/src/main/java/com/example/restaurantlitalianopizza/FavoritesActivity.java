package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle clicks on the hamburger icon
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        LinearLayout layout = findViewById(R.id.layout_favorite);
        displayFavoritePizzas(layout);
    }

    private void displayFavoritePizzas(LinearLayout layout) {
        ArrayList<Pizza> favoritePizzas = NewPizzasSave.getInstance().getFavorites();

        for (Pizza pizza : favoritePizzas) {
            layout.addView(createPizzaView(pizza));
        }
    }

    private LinearLayout createPizzaView(final Pizza pizza) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout pizzaLayout = new LinearLayout(this);
        pizzaLayout.setOrientation(LinearLayout.VERTICAL);
        pizzaLayout.setLayoutParams(layoutParams);

        // Create ImageView for pizza image
        ImageView pizzaImage = new ImageView(this);
        int imageResourceId = getResources().getIdentifier(pizza.getImage(), "drawable", getPackageName());
        pizzaImage.setImageResource(imageResourceId);
        int imageWidth = getResources().getDisplayMetrics().widthPixels;
        int imageHeight = (int) (imageWidth * 0.6); // Aspect ratio 3:5
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                imageWidth,
                imageHeight
        );
        imageLayoutParams.gravity = Gravity.CENTER;
        pizzaImage.setLayoutParams(imageLayoutParams);
        pizzaImage.setAdjustViewBounds(true);
        pizzaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Create TextView for pizza name
        TextView pizzaName = new TextView(this);
        pizzaName.setText(pizza.getName());
        pizzaName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        pizzaName.setPadding(0, 16, 0, 8);
        pizzaName.setGravity(Gravity.CENTER);

        // Create TextView for pizza price
        TextView pizzaPrice = new TextView(this);
        pizzaPrice.setText("$" + pizza.getPrice());
        pizzaPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaPrice.setPadding(0, 0, 0, 16);
        pizzaPrice.setGravity(Gravity.CENTER);

        // Button layout parameters for a bit wider buttons and centered
        int buttonWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.4); // 40% of screen width
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                buttonWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.gravity = Gravity.CENTER;
        buttonLayoutParams.setMargins(8, 8, 8, 8); // Optional: Add margins for spacing

        // Create order button
        Button orderButton = new Button(this);
        orderButton.setText("Order Now");
        orderButton.setLayoutParams(buttonLayoutParams);
        orderButton.setTextColor(Color.WHITE);
        orderButton.setBackgroundColor(Color.parseColor("#008000"));
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement order functionality
                Intent orderIntent = new Intent(FavoritesActivity.this, CompleteOrderActivity.class);
                orderIntent.putExtra("ordered_pizza", pizza);
                startActivity(orderIntent);
            }
        });

        // Create undo favorite button
        Button undoFavoriteButton = new Button(this);
        undoFavoriteButton.setText("Undo Favorite");
        undoFavoriteButton.setLayoutParams(buttonLayoutParams);
        undoFavoriteButton.setTextColor(Color.WHITE);
        undoFavoriteButton.setBackgroundColor(Color.parseColor("#008000"));
        undoFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement undo favorite functionality
                NewPizzasSave.getInstance().removeFromFavorites(pizza);
                recreate();
            }
        });

        // Create a horizontal layout to center buttons
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.addView(orderButton);
        buttonLayout.addView(undoFavoriteButton);

        // Add views to the pizza layout
        pizzaLayout.addView(pizzaImage);
        pizzaLayout.addView(pizzaName);
        pizzaLayout.addView(pizzaPrice);
        pizzaLayout.addView(buttonLayout);

        return pizzaLayout;
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
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(FavoritesActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(FavoritesActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(FavoritesActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(FavoritesActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(FavoritesActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(FavoritesActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(FavoritesActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(FavoritesActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
