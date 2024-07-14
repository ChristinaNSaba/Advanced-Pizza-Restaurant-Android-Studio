package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private CheckBox checkBoxRememberMe;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;


    private static final String SHARED_PREF_NAME = "user_credentials";
    private static final String PREF_REMEMBER_ME = "rememberMe";
    private static final String PREF_EMAIL = "email";
    private Map<String, Map<String, Object>> pizzaTypes = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        db = new DatabaseHelper(this);

        boolean rememberMe = sharedPreferences.getBoolean(PREF_REMEMBER_ME, false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString(PREF_EMAIL, "");
            editTextEmail.setText(savedEmail);
            checkBoxRememberMe.setChecked(true);
        }

        Button signup = findViewById(R.id.buttonSignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String storedPassword = db.getPassword(email);

        if (TextUtils.isEmpty(storedPassword)) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (encryptPassword(password).equals(storedPassword)) {
            if (checkBoxRememberMe.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_EMAIL, email);
                editor.putBoolean(PREF_REMEMBER_ME, true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(PREF_EMAIL);
                editor.putBoolean(PREF_REMEMBER_ME, false);
                editor.apply();
            }
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            boolean isAdmin = db.isAdmin(email, encryptPassword(password));
            Intent intent;
            if (isAdmin) {
                intent = new Intent(LoginActivity.this, AdminProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                fillPizzas();
                db = new DatabaseHelper(this);
                String name = db.getUserNameByEmail(email);
                NewPizzasSave.getInstance().AddUser("name", name);
                intent = new Intent(LoginActivity.this, WelcomePageActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void fillPizzas(){
        setupPizzaTypes();
        populatePizzas();
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
                    NewPizzasSave.getInstance().getPizzaArrayList().add(pizza);
                    switch (category) {
                        case "Chicken":
                            NewPizzasSave.getInstance().addChicken(pizza);
                            break;
                        case "Beef":
                            NewPizzasSave.getInstance().addBeef(pizza);
                            break;
                        case "Viggies":
                            NewPizzasSave.getInstance().addViggies(pizza);
                            break;
                        case "Seafood":
                            NewPizzasSave.getInstance().addSeafood(pizza);
                            break;
                    }
                    if (price == 4.99) {
                        NewPizzasSave.getInstance().addPrice499(pizza);
                    }
                    else if (price == 9.99) {
                        NewPizzasSave.getInstance().addPrice999(pizza);
                    }
                    else if (price == 14.99) {
                        NewPizzasSave.getInstance().addPrice1499(pizza);
                    }
                    else {
                        NewPizzasSave.getInstance().addPrice1999(pizza);
                    }
                }
            }
        }
    }
}
