package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MenuFilteredActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ArrayList<Pizza> filteredPizzas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_filtered);

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
        toggle.syncState();

        String filterValue = getIntent().getStringExtra("type");

        LinearLayout layout = findViewById(R.id.layout_menu_filtered_activity);

        if (filterValue != null) {
            switch (filterValue) {
                case "Viggies":
                    filteredPizzas = NewPizzasSave.getInstance().getViggies();
                    break;
                case "beef":
                    filteredPizzas = NewPizzasSave.getInstance().getBeef();
                    break;
                case "chicken":
                    filteredPizzas = NewPizzasSave.getInstance().getChicken();
                    break;
                case "seafood":
                    filteredPizzas = NewPizzasSave.getInstance().getSeafood();
                    break;
                case "4.99":
                    filteredPizzas = NewPizzasSave.getInstance().getPrice499();
                    break;
                case "9.99":
                    filteredPizzas = NewPizzasSave.getInstance().getPrice999();
                    break;
                case "14.99":
                    filteredPizzas = NewPizzasSave.getInstance().getPrice1499();
                    break;
                case "19.99":
                    filteredPizzas = NewPizzasSave.getInstance().getPrice1999();
                    break;
                default:
                    filteredPizzas = new ArrayList<>(); // Handle default case
            }

            // Add pizzas to the layout
            addPizzasToLayout(layout, filteredPizzas);
        }
    }

    private void addPizzasToLayout(LinearLayout layout, ArrayList<Pizza> pizzas) {
        for (Pizza pizza : pizzas) {
            layout.addView(createPizzaView(pizza));
        }
    }

    private LinearLayout createPizzaView(Pizza pizza) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
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
        pizzaName.setText(pizza.getName());
        pizzaName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        pizzaName.setPadding(0, 16, 0, 8);
        pizzaName.setGravity(Gravity.CENTER);

        TextView pizzaPrice = new TextView(this);
        pizzaPrice.setText("$" + pizza.getPrice());
        pizzaPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pizzaPrice.setPadding(0, 0, 0, 16);
        pizzaPrice.setGravity(Gravity.CENTER);

        int buttonWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.4); // 40% of screen width

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                buttonWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.gravity = Gravity.CENTER;
        buttonLayoutParams.setMargins(8, 8, 8, 8); // Optional: Add margins for spacing

        Button favoritesButton = new Button(this);
        favoritesButton.setText("Add to Favorites");
        favoritesButton.setLayoutParams(buttonLayoutParams);
        favoritesButton.setTextColor(Color.WHITE);
        favoritesButton.setBackgroundColor(Color.parseColor("#008000"));
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites(pizza);
            }
        });

        Button ordersButton = new Button(this);
        ordersButton.setText("Add to Orders");
        ordersButton.setLayoutParams(buttonLayoutParams);
        ordersButton.setTextColor(Color.WHITE);
        ordersButton.setBackgroundColor(Color.parseColor("#008000"));
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrders(pizza);
            }
        });

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.addView(favoritesButton);
        buttonLayout.addView(ordersButton);

        pizzaLayout.addView(pizzaImage);
        pizzaLayout.addView(pizzaName);
        pizzaLayout.addView(pizzaPrice);
        pizzaLayout.addView(buttonLayout);

        pizzaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fragview = new Intent(MenuFilteredActivity.this, PizzaFragmentView.class);
                fragview.putExtra("pizza_name", pizza.getName());
                fragview.putExtra("pizza_price", pizza.getPrice());
                fragview.putExtra("pizza_image", pizza.getImage());
                fragview.putExtra("pizza_description", pizza.getCategory());
                startActivity(fragview);
            }
        });
        return pizzaLayout;
    }




    private void addToFavorites(Pizza pizza) {

        NewPizzasSave.getInstance().addFavoritePizza(pizza);
    }

    private void addToOrders(Pizza pizza) {
        Intent complete = new Intent(MenuFilteredActivity.this, CompleteOrderActivity.class);
        complete.putExtra("ordered_pizza", pizza);
        startActivity(complete);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(MenuFilteredActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(MenuFilteredActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(MenuFilteredActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(MenuFilteredActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(MenuFilteredActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(MenuFilteredActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(MenuFilteredActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(MenuFilteredActivity.this, LoginActivity.class);
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
