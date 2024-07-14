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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PizzaMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Pizza> pizzas = new ArrayList<>();
    private Map<String, Map<String, Object>> pizzaTypes = new HashMap<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_menu);

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
        setupPizzaTypes();
        populatePizzas();

        LinearLayout layout = findViewById(R.id.layout_menu);
        addPizzasToLayout(layout);
    }

    private void setupPizzaTypes() {
        addPizzaType("Margarita", "margarita", 9.99, "Margherita pizza is simple and delicious. A classic Italian thin-crust topped with fresh tomato sauce, basil, and mozzarella cheese.", "Viggies");
        addPizzaType("Neapolitan", "neapolitan", 14.99, "Neapolitan pizza is a traditional style with simple ingredients. Neapolitan pizza typically has minimal toppings. The classic version features San Marzano tomatoes, which are known for their sweetness and low acidity, fresh mozzarella cheese made from buffalo milk, basil, extra virgin olive oil, and a sprinkle of sea salt.", "Viggies");
        addPizzaType("Hawaiian", "hawaiian", 9.99, "Hawaiian pizza combines sweet and savory flavors. it's topped with thinly sliced ham and chunks of pineapple, with the sweetness of the pineapple complementing the salty richness of the ham.", "Beef");
        addPizzaType("Pepperoni", "pepperoni", 9.99, "Pepperoni pizza has perfect balance of flavors and textures. It is topped with slices of pepperoni, a type of cured and seasoned sausage made from beef, with mozarella cheese.", "Beef");
        addPizzaType("NewYorkStyle", "newyorkstyle", 4.99, "NewYork Style pizza is iconic and delicious. A classic New York slice is typically topped with tomato sauce and shredded mozzarella cheese with large, thin crust that is crisp on the outside and chewy on the inside.", "Viggies");
        addPizzaType("Calzone", "calzone", 14.99, "Calzone is a delicious Italian dish that's a folded pizza. The fillings include ricotta cheese, mozzarella cheese, cooked meats such as sausage or pepperoni, vegetables like mushrooms or bell peppers, and if wanted tomato sauce.", "Chicken");
        addPizzaType("TandooriChicken", "tandoorichickenpizza", 14.99, "Tandoori Chicken Pizza combines the flavors of traditional Indian cuisine with the popular Italian pizza. The pizza dough is rolled out into a thin crust, topped by tandoori chicken is marinated in a mixture of yogurt and tandoori spice blend, which includes spices like cumin, turmeric, paprika, and garam masala, with garlic, ginger, lemon juice, onion slices, tomatoes, bell pepper, and cheese.", "Chicken");
        addPizzaType("BBQChicken", "bbqchickenpizza", 9.99, "BBQ Chicken Pizza is an iconic mixture of barbecue sauce along with tender pieces of chicken. The pizza dough is rolled out into a thin crust, topped by BBQ sauce and chicken and others like red onion, bell peppers, and cilantro.", "Chicken");
        addPizzaType("Seafood", "seafood", 19.99, "Seafood Pizza is a delightful variation of the classic dish, combining the savory flavors of the ocean with the familiarity of pizza. It is topped with creamy white sauce, shrimps or clams or mussels or calamari or crab by choice, onions, and peppers.", "Seafood");
        addPizzaType("Vegetarian", "viggies", 9.99, "Vegetarian Pizza offers a delicious and satisfying option for vegetarians. It’s fresh and full of flavor, featuring cherry tomatoes, artichoke, bell pepper, olives, red onion and some hidden (and optional) baby spinach. You’ll find a base of rich tomato sauce and golden, bubbling mozzarella underneath.", "Viggies");
        addPizzaType("Buffalo", "buffalojpg", 14.99, "Buffalo Chicken pizza is a delicious option topped with the juicy meat, melted butter, the signature spicy sauce, the tangy blue cheese, sliced onions, bell peppers, jalapeños, or diced celery for a crunchy texture (optional).", "Chicken");
        addPizzaType("MushroomTruffle", "mushroom", 9.99, "Mushroom Truffle Pizza is simple and delicious. Fresh mushrooms and fresh mozzarella are layered on top of a concentrated mushroom sauce and then drizzled with truffle oil with additional toppings of caramelized onions, roasted garlic, fresh thyme, or a sprinkle of grated Parmesan cheese.","Viggies");
        addPizzaType("PestoChicken", "pesto", 14.99, "Pesto Chicken Pizza is a mouthwatering combination of tender chicken, pesto sauce, and classic pizza ingredients. Pesto is made from fresh basil leaves, pine nuts, garlic, Parmesan cheese, and olive oil blended together into a smooth and vibrant sauce. The shredded chicken breast is seasoned then spread over the pizza with ripe tomatoes, basil pesto, and melty mozzarella.", "Chicken");
    }

    private void addPizzaType(String name, String image, double price, String description, String category) {
        Map<String, Object> pizzaData = new HashMap<>();
        pizzaData.put("image", image);
        pizzaData.put("price", price);
        pizzaData.put("description", description);
        pizzaData.put("category", category);
        pizzaTypes.put(name, pizzaData);
    }

    private void populatePizzas() {
        for (String pizzaName : pizzaTypes.keySet()) {
            Map<String, Object> pizzaData = pizzaTypes.get(pizzaName);
            if (pizzaData != null) {
                String description = (String) pizzaData.get("description");
                Double price = (Double) pizzaData.get("price");
                String image = (String) pizzaData.get("image");
                String category = (String) pizzaData.get("category");

                if (description != null && price != null && image != null && category != null) {
                    Pizza pizza = new Pizza(pizzaName, description, price, image, category);
                    NewPizzasSave.getInstance().getPizzaArrayList().add(pizza); // Save to pizzaArrayList
                    pizzas.add(pizza);
                }
            }
        }
    }

    private void addPizzasToLayout(LinearLayout layout) {
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
                Intent fragview = new Intent(PizzaMenuActivity.this, PizzaFragmentView.class);
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
        Intent complete = new Intent(PizzaMenuActivity.this, CompleteOrderActivity.class);
        complete.putExtra("ordered_pizza", pizza);
        startActivity(complete);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent welcomeIntent = new Intent(PizzaMenuActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(PizzaMenuActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(PizzaMenuActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(PizzaMenuActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(PizzaMenuActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        } else if (id == R.id.profile) {
            Intent profile = new Intent(PizzaMenuActivity.this, ProfileActivity.class);
            startActivity(profile);
        } else if (id == R.id.callusorfindus) {
            Intent call = new Intent(PizzaMenuActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(PizzaMenuActivity.this, LoginActivity.class);
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
