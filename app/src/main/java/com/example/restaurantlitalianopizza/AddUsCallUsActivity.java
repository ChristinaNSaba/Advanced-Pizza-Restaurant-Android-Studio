package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AddUsCallUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String PHONE_NUMBER = "0599000000";
    private static final String MAPS_COORDINATES = "geo:31.961013,35.190483";
    private static final String EMAIL_ADDRESS = "AdvancePizza@Pizza.com";
    private static final String EMAIL_SUBJECT = "Subject: Pizza Inquiry";
    private static final String EMAIL_BODY = "Content of the email";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_us_call_us);

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
        Button dialButton = findViewById(R.id.button_dial);
        Button gmailButton = findViewById(R.id.button_gmail);
        Button mapsButton = findViewById(R.id.button_maps);

        dialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                startActivity(dialIntent);
            }
        });

        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent = new Intent(Intent.ACTION_SENDTO);
                gmailIntent.setData(Uri.parse("mailto:" + EMAIL_ADDRESS));
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                gmailIntent.putExtra(Intent.EXTRA_TEXT, EMAIL_BODY);
                startActivity(gmailIntent);
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent();
                mapsIntent.setAction(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse(MAPS_COORDINATES));
                startActivity(mapsIntent);
            }
        });
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
            Intent welcomeIntent = new Intent(AddUsCallUsActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(AddUsCallUsActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(AddUsCallUsActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(AddUsCallUsActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(AddUsCallUsActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        }
        else if(id == R.id.profile){
            Intent profile = new Intent(AddUsCallUsActivity.this, ProfileActivity.class);
            startActivity(profile);
        }
        else if(id == R.id.callusorfindus){
            Intent call = new Intent(AddUsCallUsActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        }
        else if(id == R.id.logout){
            Intent logout = new Intent(AddUsCallUsActivity.this, LoginActivity.class);
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
