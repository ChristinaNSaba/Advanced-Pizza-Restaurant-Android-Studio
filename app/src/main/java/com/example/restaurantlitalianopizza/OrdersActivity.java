package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class OrdersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

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

        LinearLayout layout = findViewById(R.id.layout_order);
        displayOrderedPizzas(layout);
    }

    private void displayOrderedPizzas(LinearLayout layout) {
        ArrayList<Order> orderedPizzas = NewPizzasSave.getInstance().getOrders();

        for (Order pizza : orderedPizzas) {
            layout.addView(createPizzaView(pizza));
        }
    }

    private LinearLayout createPizzaView(final Order pizza) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout pizzaLayout = new LinearLayout(this);
        pizzaLayout.setOrientation(LinearLayout.VERTICAL);
        pizzaLayout.setLayoutParams(layoutParams);

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

        TextView pizzaName = new TextView(this);
        pizzaName.setText("Name: " + pizza.getName());
        pizzaName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        pizzaName.setPadding(0, 16, 0, 8);
        pizzaName.setGravity(Gravity.CENTER);

        TextView pizzaPrice = new TextView(this);
        pizzaPrice.setText("Price: $" + pizza.getPrice());
        pizzaPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaPrice.setPadding(0, 0, 0, 16);
        pizzaPrice.setGravity(Gravity.CENTER);

        pizzaLayout.addView(pizzaImage);
        pizzaLayout.addView(pizzaName);
        pizzaLayout.addView(pizzaPrice);

        pizzaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, OrderFragmentView.class);
                intent.putExtra("order_name", pizza.getName());
                intent.putExtra("order_price", pizza.getPrice());
                intent.putExtra("order_type", pizza.getType());
                intent.putExtra("order_size", pizza.getSize());
                intent.putExtra("order_quantity", pizza.getQuantity());
                intent.putExtra("order_date", pizza.getDate());
                intent.putExtra("order_image", pizza.getImage());
                startActivity(intent);
            }
        });

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
            Intent welcomeIntent = new Intent(OrdersActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(OrdersActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(OrdersActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(OrdersActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(OrdersActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(OrdersActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(OrdersActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(OrdersActivity.this, LoginActivity.class);
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
