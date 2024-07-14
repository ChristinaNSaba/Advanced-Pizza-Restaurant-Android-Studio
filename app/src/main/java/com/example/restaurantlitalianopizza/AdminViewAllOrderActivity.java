package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.content.res.Configuration;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminViewAllOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_order);
        LinearLayout layout = findViewById(R.id.layout_view_all_orders);

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

        displayAllOrders(layout);
        addDivider(layout);
        displayOrderStatistics(layout);
    }

    private void displayAllOrders(LinearLayout layout) {
        ArrayList<Order> allOrders = NewPizzasSave.getInstance().getAllOrders();

        for (Order pizza : allOrders) {
            layout.addView(createPizzaView(pizza));
        }
    }

    private LinearLayout createPizzaView(Order pizza) {
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

        TextView pizzaType = new TextView(this);
        pizzaType.setText("Type: " + pizza.getType());
        pizzaType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaType.setPadding(0, 0, 0, 8);
        pizzaType.setGravity(Gravity.CENTER);

        TextView pizzaSize = new TextView(this);
        pizzaSize.setText("Size: " + pizza.getSize());
        pizzaSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaSize.setPadding(0, 0, 0, 8);
        pizzaSize.setGravity(Gravity.CENTER);

        TextView pizzaQuantity = new TextView(this);
        pizzaQuantity.setText("Quantity: " + pizza.getQuantity());
        pizzaQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaQuantity.setPadding(0, 0, 0, 8);
        pizzaQuantity.setGravity(Gravity.CENTER);

        TextView pizzaPrice = new TextView(this);
        pizzaPrice.setText("Price: $" + pizza.getPrice());
        pizzaPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaPrice.setPadding(0, 0, 0, 16);
        pizzaPrice.setGravity(Gravity.CENTER);

        TextView orderDate = new TextView(this);
        orderDate.setText("Order Date: " + pizza.getDate());
        orderDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        orderDate.setPadding(0, 0, 0, 8);
        orderDate.setGravity(Gravity.CENTER);

        TextView orderUser = new TextView(this);
        orderUser.setText("Ordered By: " + pizza.getUsername());
        orderUser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        orderUser.setPadding(0, 0, 0, 8);
        orderUser.setGravity(Gravity.CENTER);

        pizzaLayout.addView(pizzaImage);
        pizzaLayout.addView(pizzaName);
        pizzaLayout.addView(pizzaType);
        pizzaLayout.addView(pizzaSize);
        pizzaLayout.addView(pizzaQuantity);
        pizzaLayout.addView(pizzaPrice);
        pizzaLayout.addView(orderDate);
        pizzaLayout.addView(orderUser);

        return pizzaLayout;
    }

    private void addDivider(LinearLayout layout) {
        View divider = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                4 // Divider height
        );
        params.setMargins(0, 16, 0, 16); // Margin around the divider
        divider.setLayoutParams(params);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        layout.addView(divider);
    }

    private void displayOrderStatistics(LinearLayout layout) {
        ArrayList<Order> allOrders = NewPizzasSave.getInstance().getAllOrders();
        HashMap<String, Integer> orderCountByType = new HashMap<>();
        HashMap<String, Double> incomeByType = new HashMap<>();
        double totalIncome = 0.0;

        for (Order pizza : allOrders) {
            String name = pizza.getName();
            int quantity = pizza.getQuantity();
            double income = pizza.getPrice();

            orderCountByType.put(name, orderCountByType.getOrDefault(name, 0) + quantity);
            incomeByType.put(name, incomeByType.getOrDefault(name, 0.0) + income);
            totalIncome += income;
        }

        // Display the statistics
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView orderStatsHeader = new TextView(this);
        orderStatsHeader.setText("Order Statistics:");
        orderStatsHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        orderStatsHeader.setPadding(0, 16, 0, 16);
        orderStatsHeader.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        orderStatsHeader.setGravity(Gravity.CENTER);
        layout.addView(orderStatsHeader);

        for (Map.Entry<String, Integer> entry : orderCountByType.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();
            double income = incomeByType.get(type);

            TextView statsTextView = new TextView(this);
            statsTextView.setText("Type: " + type + ", Orders: " + count + ", Income: $" + String.format("%.2f", income));
            statsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            statsTextView.setPadding(0, 0, 0, 8);
            statsTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            statsTextView.setGravity(Gravity.CENTER);
            layout.addView(statsTextView);
        }

        TextView totalIncomeTextView = new TextView(this);
        totalIncomeTextView.setText("Total Income: $" + String.format("%.2f", totalIncome));
        totalIncomeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        totalIncomeTextView.setPadding(0, 16, 0, 16);
        totalIncomeTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        totalIncomeTextView.setGravity(Gravity.CENTER);
        layout.addView(totalIncomeTextView);
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
            Intent welcomeIntent = new Intent(AdminViewAllOrderActivity.this, AdminProfileActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.addadmin) {
            Intent menu = new Intent(AdminViewAllOrderActivity.this, AddAdminActivity.class);
            startActivity(menu);
        } else if (id == R.id.addspecialoffers) {
            Intent favorites = new Intent(AdminViewAllOrderActivity.this, AdminAddSpecialOffersActivity.class);
            startActivity(favorites);
        } else if (id == R.id.viewallorders) {
            Intent orders = new Intent(AdminViewAllOrderActivity.this, AdminViewAllOrderActivity.class);
            startActivity(orders);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(AdminViewAllOrderActivity.this, LoginActivity.class);
            startActivity(logout);
            finish();
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}
