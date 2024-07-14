package com.example.restaurantlitalianopizza;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LItalianoPizza";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_SPECIAL_OFFERS = "special_offers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_PASSWORD = "hashedPassword";
    private static final String COLUMN_USER_TYPE = "userType";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture";
    private static final String COLUMN_IS_ADMIN = "isAdmin";
    private static final String COLUMN_OFFER_ID = "id";
    private static final String COLUMN_PIZZA_TYPE = "pizza_type";
    private static final String COLUMN_PIZZA_SIZE = "pizza_size";
    private static final String COLUMN_OFFER_PERIOD = "offer_period";
    private static final String COLUMN_TOTAL_PRICE = "total_price";

    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_CUSTOMER_EMAIL = "customer_email";
    private static final String COLUMN_PIZZA_NAME = "pizza_name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGE_RESOURCE = "imageResource";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY = "category";

    private static final String SHARED_PREF_NAME = "user_credentials";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PHONE + " TEXT NOT NULL, " +
                    COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    COLUMN_GENDER + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_PICTURE + " TEXT NOT NULL, " +
                    COLUMN_IS_ADMIN + " TEXT NOT NULL);";

    private static final String TABLE_CREATE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CUSTOMER_EMAIL + " INTEGER NOT NULL, " +
                    COLUMN_PIZZA_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_SIZE + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_RESOURCE + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_CUSTOMER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + ")" +
                    ");";

    String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE " + TABLE_SPECIAL_OFFERS + " (" +
            COLUMN_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PIZZA_TYPE + " TEXT, " +
            COLUMN_PIZZA_SIZE + " TEXT, " +
            COLUMN_OFFER_PERIOD + " TEXT, " +
            COLUMN_TOTAL_PRICE + " REAL" +
            ")";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
        createInitialAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIAL_OFFERS);
        onCreate(db);
    }

    private void createInitialAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, "admin@gmail.com");
        values.put(COLUMN_PHONE, "0500000000");
        values.put(COLUMN_FIRST_NAME, "Admin");
        values.put(COLUMN_LAST_NAME, "User");
        values.put(COLUMN_GENDER, "Male");
        values.put(COLUMN_PROFILE_PICTURE, "android.resource://com.example.pizzarestaurantapplication/drawable/user");
        values.put(COLUMN_PASSWORD, encryptPassword("Admin123")); // Encrypt the password
        values.put(COLUMN_IS_ADMIN, 1);
        db.insert(TABLE_USERS, null, values);
        editor.putString("admin@gmail.com", encryptPassword("Admin123"));
        editor.apply();
    }

    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_PASSWORD, encryptPassword(user.getPassword()));
        values.put(COLUMN_PROFILE_PICTURE, user.getProfilePicture());
        values.put(COLUMN_IS_ADMIN, 0);

        db.insert(TABLE_USERS, null, values);

        editor.putString(user.getEmail(), user.getPassword());
        editor.apply();
    }
    public String getPassword(String email) {
        return sharedPreferences.getString(email, "");
    }


    public boolean isAdmin(String email, String hashedPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int isAdminNum = 0; // Default value

        try {
            // Query to fetch isAdmin for the given email and hashed password
            String query = "SELECT " + COLUMN_IS_ADMIN + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";

            cursor = db.rawQuery(query, new String[]{email, hashedPassword});

            if (cursor != null && cursor.moveToFirst()) {
                // Read isAdmin value from cursor
                isAdminNum = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close cursor and database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return isAdminNum == 1;
    }


    public void insertAdmin(User admin, int isAdmin) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, admin.getEmail());
            values.put(COLUMN_PHONE, admin.getPhone());
            values.put(COLUMN_FIRST_NAME, admin.getFirstName());
            values.put(COLUMN_LAST_NAME, admin.getLastName());
            values.put(COLUMN_GENDER, admin.getGender());
            values.put(COLUMN_PASSWORD, admin.getPassword());
            values.put(COLUMN_PROFILE_PICTURE, admin.getProfilePicture());
            values.put(COLUMN_IS_ADMIN, isAdmin);

            long id = db.insert(TABLE_USERS, null, values);
            editor.putString(admin.getEmail(), admin.getPassword());
            editor.apply();
            if (id == -1) {
                // Handle the error
                Log.e("DatabaseHelper", "Error inserting new admin");
            } else {
                Log.i("DatabaseHelper", "Admin inserted with ID: " + id);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Exception while inserting admin", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    public String getUserFirstName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_FIRST_NAME}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            if (columnIndex != -1) {
                String firstName = cursor.getString(columnIndex);
                cursor.close();
                return firstName;
            }
            cursor.close();
        }
        return null;
    }
    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_FIRST_NAME, COLUMN_LAST_NAME}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        String name = null;
        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST_NAME);
            if (firstNameIndex != -1 && lastNameIndex != -1) {
                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                name = firstName + " " + lastName;
            }
            cursor.close();
        }
        db.close();
        return name;
    }

    public void addOrder(int customerId, String pizzaName, int quantity, String size, double price, String date, String description, String imageResource, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_EMAIL, customerId);
        values.put(COLUMN_PIZZA_NAME, pizzaName);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE_RESOURCE, imageResource);
        values.put(COLUMN_CATEGORY, category);

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }
    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORDERS, null, null, null, null, null, null);
    }



    public Cursor getOrdersByCustomerEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORDERS, null, COLUMN_CUSTOMER_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    public String encryptPassword(String password) {
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

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String profilePicture = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE));
            user = new User(userEmail, phone, firstName, lastName, gender, password, profilePicture);
            cursor.close();
        }
        return user;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_PROFILE_PICTURE, user.getProfilePicture());

        if (user.getPassword() != null) {
            values.put(COLUMN_PASSWORD, encryptPassword(user.getPassword()));
            editor.putString(user.getEmail(), user.getPassword());
            editor.apply();
        }

        db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{user.getEmail()});
    }


    // Methods to manage special offers

    public boolean insertSpecialOffer(String pizzaType, String pizzaSize, String offerPeriod, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PIZZA_TYPE, pizzaType);
        contentValues.put(COLUMN_PIZZA_SIZE, pizzaSize);
        contentValues.put(COLUMN_OFFER_PERIOD, offerPeriod);
        contentValues.put(COLUMN_TOTAL_PRICE, totalPrice);

        long result = db.insert(TABLE_SPECIAL_OFFERS, null, contentValues);
        return result != -1; // returns true if insertion is successful
    }

    public Cursor getAllSpecialOffers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SPECIAL_OFFERS, null);
    }

    public boolean updateSpecialOffer(int id, String pizzaType, String pizzaSize, String offerPeriod, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PIZZA_TYPE, pizzaType);
        contentValues.put(COLUMN_PIZZA_SIZE, pizzaSize);
        contentValues.put(COLUMN_OFFER_PERIOD, offerPeriod);
        contentValues.put(COLUMN_TOTAL_PRICE, totalPrice);
        int result = db.update(TABLE_SPECIAL_OFFERS, contentValues, COLUMN_OFFER_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Integer deleteSpecialOffer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SPECIAL_OFFERS, COLUMN_OFFER_ID + "=?", new String[]{String.valueOf(id)});
    }



}
