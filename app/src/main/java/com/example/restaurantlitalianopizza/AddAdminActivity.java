package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Spinner spinnerGender;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private ImageButton buttonChangePicture;
    private ImageView imageViewUser;
    private DatabaseHelper db;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String encodedImage; // To store the profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonChangePicture = findViewById(R.id.buttonChangePicture);
        imageViewUser = findViewById(R.id.imageViewUser);

        db = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setSelection(adapter.getPosition("Gender"));

        // Set default profile picture
        imageViewUser.setImageResource(R.drawable.user);
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        encodedImage = encodeToBase64(defaultBitmap);

        buttonChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addAdmin();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageViewUser.setImageBitmap(bitmap);

                // Encode image to Base64
                encodedImage = encodeToBase64(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!isValidEmail(email)) {
            editTextEmail.setError("Invalid email");
            return false;
        }

        if (!isValidPhone(phone)) {
            editTextPhone.setError("Phone number must be 10 digits starting with '05'");
            return false;
        }

        if (firstName.length() < 3) {
            editTextFirstName.setError("First name must be at least 3 characters");
            return false;
        }

        if (lastName.length() < 3) {
            editTextLastName.setError("Last name must be at least 3 characters");
            return false;
        }

        if (!isValidPassword(password)) {
            editTextPassword.setError("Password must be at least 8 characters and include at least 1 letter and 1 number");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^05\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        return letter.matcher(password).find() && digit.matcher(password).find();
    }

    private void addAdmin() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String password = editTextPassword.getText().toString().trim();

        String encryptedPassword = encryptPassword(password);
        if (TextUtils.isEmpty(encodedImage)) {
            // Set encodedImage to the default image
            encodedImage = encodeToBase64(BitmapFactory.decodeResource(getResources(), R.drawable.user));
        }

        // Create a User object without isAdmin attribute
        User admin = new User(email, phone, firstName, lastName, gender, encryptedPassword, encodedImage);
        db.insertAdmin(admin, 1);
        Toast.makeText(this, "Admin added successfully", Toast.LENGTH_SHORT).show();
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

    private String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
            Intent welcomeIntent = new Intent(AddAdminActivity.this, AdminProfileActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.addadmin) {
            Intent menu = new Intent(AddAdminActivity.this, AddAdminActivity.class);
            startActivity(menu);
        } else if (id == R.id.addspecialoffers) {
            Intent favorites = new Intent(AddAdminActivity.this, AdminAddSpecialOffersActivity.class);
            startActivity(favorites);
        } else if (id == R.id.viewallorders) {
            Intent orders = new Intent(AddAdminActivity.this, AdminViewAllOrderActivity.class);
            startActivity(orders);
        } else if (id == R.id.logout) {
            Intent logout = new Intent(AddAdminActivity.this, LoginActivity.class);
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
