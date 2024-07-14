package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class WelcomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        drawerLayout = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(WelcomePageActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(WelcomePageActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        }
        else if(id == R.id.yourfavorites){
            Intent favorites = new Intent(WelcomePageActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        }
        else if(id == R.id.orders){
            Intent orders = new Intent(WelcomePageActivity.this, OrdersActivity.class);
            startActivity(orders);
        }
        else if(id == R.id.specialoffers){
            Intent hotdeals = new Intent(WelcomePageActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        }
        else if(id == R.id.profile){
            Intent profile = new Intent(WelcomePageActivity.this, ProfileActivity.class);
            startActivity(profile);
        }
        else if(id == R.id.callusorfindus){
            Intent call = new Intent(WelcomePageActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        }
        else if(id == R.id.logout){
            Intent logout = new Intent(WelcomePageActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
