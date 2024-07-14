package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final String SHARED_PREF_NAME = "user_credentials";
    private static final String PREF_EMAIL = "email";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView imageViewUser;
    private ImageButton buttonChangePicture;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Spinner spinnerGender;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSave;
    private DatabaseHelper db;
    private User currentUser;
    private String encodedImage; // To store the profile picture
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        imageViewUser = findViewById(R.id.imageViewUser);
        buttonChangePicture = findViewById(R.id.buttonChangePicture);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSave = findViewById(R.id.buttonSave);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(PREF_EMAIL, null);

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "No user email found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        db = new DatabaseHelper(this);
        currentUser = db.getUser(userEmail);

        if (currentUser == null) {
            Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setFocusable(false);
        spinnerGender.setClickable(false);
        spinnerGender.setEnabled(false);

        if (currentUser != null) {
            displayUserInfo(currentUser);
        }

        buttonChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayUserInfo(User user) {
        editTextEmail.setText(user.getEmail());
        editTextEmail.setEnabled(false);
        editTextPhone.setText(user.getPhone());
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        spinnerGender.setSelection(((ArrayAdapter<String>) spinnerGender.getAdapter()).getPosition(user.getGender()));
        // Make spinner non-interactive and disabled
        spinnerGender.setFocusable(false);
        spinnerGender.setClickable(false);
        spinnerGender.setEnabled(false);
        // Decode the profile picture and set it in the ImageView
        String profilePicture = user.getProfilePicture();
        if (!TextUtils.isEmpty(profilePicture)) {
            byte[] decodedString = Base64.decode(profilePicture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewUser.setImageBitmap(decodedByte);
        } else {
            imageViewUser.setImageResource(R.drawable.user);
        }
    }

    private void saveUserInfo() {
        String phone = editTextPhone.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!validateInputs(phone, firstName, lastName, password, confirmPassword)) {
            return;
        }

        // Update user details
        currentUser.setPhone(phone);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setGender(gender);

        if (!TextUtils.isEmpty(password)) {
            currentUser.setPassword(encryptPassword(password));
        }
        if (!TextUtils.isEmpty(encodedImage)) {
            currentUser.setProfilePicture(encodedImage);
        }
        if (TextUtils.isEmpty(encodedImage) && TextUtils.isEmpty(currentUser.getProfilePicture())) {
            currentUser.setProfilePicture("");
        }
        db.updateUser(currentUser);

        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs(String phone, String firstName, String lastName, String password, String confirmPassword) {
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

        if (!TextUtils.isEmpty(password)) {
            if (!isValidPassword(password)) {
                editTextPassword.setError("Password must be at least 8 characters and include at least 1 letter and 1 number");
                return false;
            }

            if (!password.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Passwords do not match");
                return false;
            }
        }

        return true;
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
            Intent welcomeIntent = new Intent(ProfileActivity.this, WelcomePageActivity.class);
            startActivity(welcomeIntent);
        } else if (id == R.id.menucategories) {
            Intent menu = new Intent(ProfileActivity.this, MenuChooseActivity.class);
            startActivity(menu);
        } else if (id == R.id.yourfavorites) {
            Intent favorites = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.orders) {
            Intent orders = new Intent(ProfileActivity.this, OrdersActivity.class);
            startActivity(orders);
        } else if (id == R.id.specialoffers) {
            Intent hotdeals = new Intent(ProfileActivity.this, SpecialOffersActivity.class);
            startActivity(hotdeals);
        }
        else if(id == R.id.profile){
            Intent profile = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(profile);
        }
        else if(id == R.id.callusorfindus){
            Intent call = new Intent(ProfileActivity.this, AddUsCallUsActivity.class);
            startActivity(call);
        }
        else if(id == R.id.logout){
            Intent logout = new Intent(ProfileActivity.this, LoginActivity.class);
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
}
