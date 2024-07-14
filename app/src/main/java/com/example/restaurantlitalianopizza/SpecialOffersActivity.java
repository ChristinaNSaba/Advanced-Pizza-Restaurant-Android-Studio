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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

public class SpecialOffersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ArrayList<SpecialOffer> specialOffers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offers);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        specialOffers = NewPizzasSave.getInstance().getSpecialOffers();
        LinearLayout layoutContent = findViewById(R.id.layout_special_offers);
        populateSpecialOffers(layoutContent);
    }

    private void populateSpecialOffers(LinearLayout layoutContent) {
        if (specialOffers == null || specialOffers.isEmpty()) {
            return;
        }

        layoutContent.removeAllViews(); // Ensure the layout is cleared before populating

        for (SpecialOffer offer : specialOffers) {
            Pizza matchingPizza = findMatchingPizza(offer.getPizzaType());

            if (matchingPizza != null) {
                offer.setImage(matchingPizza.getImage());
                offer.setCategory(matchingPizza.getCategory());
                matchingPizza.setPrice(offer.getTotalPrice());
            }

            layoutContent.addView(createSpecialOfferView(offer, matchingPizza));
        }
    }

    private Pizza findMatchingPizza(String offerName) {
        for (Pizza pizza : NewPizzasSave.getInstance().getPizzaArrayList()) {
            if (pizza.getName().trim().equalsIgnoreCase(offerName.trim())) {
                return pizza;
            }
        }
        return null;
    }

    private LinearLayout createSpecialOfferView(SpecialOffer offer, Pizza matchingPizza) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout offerLayout = new LinearLayout(this);
        offerLayout.setOrientation(LinearLayout.VERTICAL);
        offerLayout.setLayoutParams(layoutParams);
        offerLayout.setPadding(16, 16, 16, 16); // Add some padding for better visuals

        ImageView imageView = new ImageView(this);
        int imageResourceId = getResources().getIdentifier(offer.getImage(), "drawable", getPackageName());
        if (imageResourceId != 0) {
            imageView.setImageResource(imageResourceId);
        }
        int imageWidth = getResources().getDisplayMetrics().widthPixels;
        int imageHeight = (int) (imageWidth * 0.6); // Adjust aspect ratio as needed
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                imageWidth,
                imageHeight
        );
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView textViewName = new TextView(this);
        textViewName.setText(offer.getPizzaType());
        textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textViewName.setPadding(0, 16, 0, 8);
        textViewName.setGravity(Gravity.CENTER);

        TextView textViewSize = new TextView(this);
        textViewSize.setText(offer.getPizzaSize());
        textViewSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textViewSize.setPadding(0, 16, 0, 8);
        textViewSize.setGravity(Gravity.CENTER);

        TextView textViewPrice = new TextView(this);
        textViewPrice.setText("$" + offer.getTotalPrice());
        textViewPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textViewPrice.setPadding(0, 0, 0, 16);
        textViewPrice.setGravity(Gravity.CENTER);

        TextView textViewPeriod = new TextView(this);
        textViewPeriod.setText("Offer Period: " + offer.getOfferPeriod());
        textViewPeriod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textViewPeriod.setPadding(0, 0, 0, 16);
        textViewPeriod.setGravity(Gravity.CENTER);

        // Button layout parameters for wider buttons and centered
        int buttonWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.4);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                buttonWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.gravity = Gravity.CENTER;
        buttonLayoutParams.setMargins(8, 8, 8, 8);

        // Create add to favorites button
        Button buttonAddToFavorites = new Button(this);
        buttonAddToFavorites.setText("Add to Favorites");
        buttonAddToFavorites.setLayoutParams(buttonLayoutParams);
        buttonAddToFavorites.setTextColor(Color.WHITE);
        buttonAddToFavorites.setBackgroundColor(Color.parseColor("#008000"));
        buttonAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchingPizza != null) {
                    NewPizzasSave.getInstance().addFavoritePizza(matchingPizza);
                }
            }
        });

        // Create order button
        Button buttonOrder = new Button(this);
        buttonOrder.setText("Order Now");
        buttonOrder.setLayoutParams(buttonLayoutParams);
        buttonOrder.setTextColor(Color.WHITE);
        buttonOrder.setBackgroundColor(Color.parseColor("#008000"));
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchingPizza != null) {
                    Intent intent = new Intent(SpecialOffersActivity.this, CompleteOrderActivity.class);
                    intent.putExtra("ordered_pizza", matchingPizza);
                    intent.putExtra("from_special_offers", true);
                    intent.putExtra("pizza_size", offer.getPizzaSize());
                    startActivity(intent);
                }
            }
        });

        // Add views to the offer layout
        offerLayout.addView(imageView);
        offerLayout.addView(textViewName);
        offerLayout.addView(textViewSize);
        offerLayout.addView(textViewPrice);
        offerLayout.addView(textViewPeriod);
        offerLayout.addView(buttonAddToFavorites);
        offerLayout.addView(buttonOrder);

        return offerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(SpecialOffersActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(SpecialOffersActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(SpecialOffersActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(SpecialOffersActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(SpecialOffersActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(SpecialOffersActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(SpecialOffersActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(SpecialOffersActivity.this, LoginActivity.class);
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
