package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuChooseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_choose);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup ActionBarDrawerToggle
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

        // Button click listeners
        Button allPizzasButton = findViewById(R.id.all_pizzas_button);
        allPizzasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, PizzaMenuActivity.class);
                startActivity(intent);
            }
        });

        Button viggiesButton = findViewById(R.id.viggies_button);
        viggiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "Viggies");
                startActivity(intent);
            }
        });

        Button beefButton = findViewById(R.id.beef_button);
        beefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "beef");
                startActivity(intent);
            }
        });

        Button chickenButton = findViewById(R.id.chicken_button);
        chickenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "chicken");
                startActivity(intent);
            }
        });

        Button seafoodButton = findViewById(R.id.seafood_button);
        seafoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "seafood");
                startActivity(intent);
            }
        });

        Button price499Button = findViewById(R.id.price_499_button);
        price499Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "4.99");
                startActivity(intent);
            }
        });

        Button price999Button = findViewById(R.id.price_999_button);
        price999Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "9.99");
                startActivity(intent);
            }
        });

        Button price1499Button = findViewById(R.id.price_1499_button);
        price1499Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "14.99");
                startActivity(intent);
            }
        });

        Button price1999Button = findViewById(R.id.price_1999_button);
        price1999Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuChooseActivity.this, MenuFilteredActivity.class);
                intent.putExtra("type", "19.99");
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(MenuChooseActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(MenuChooseActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(MenuChooseActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(MenuChooseActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(MenuChooseActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(MenuChooseActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(MenuChooseActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(MenuChooseActivity.this, LoginActivity.class);
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