package lk.nd.cycler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import lk.nd.cycler.model.LocationCard;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database";
    private static final int DATABASE_VERSION = 1;

    // User Table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_PASSWORD = "password";

    // Location Table
    public static final String TABLE_SHOP = "shop";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHOP_NAME = "shop_name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_BICYCLE_COUNT = "bicycle_count";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PRICE_PER_HOUR = "price_per_hour";

    // Rental Table
    public static final String TABLE_RENTAL = "rental";

    // SQL for creating tables
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_MOBILE + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT" +
                    ");";

    private static final String CREATE_TABLE_SHOP =
            "CREATE TABLE " + TABLE_SHOP + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SHOP_NAME + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    COLUMN_BICYCLE_COUNT + " INTEGER, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_PRICE_PER_HOUR + " INTEGER" +
                    ");";

    private static final String CREATE_TABLE_RENTAL =
            "CREATE TABLE " + TABLE_RENTAL + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "shop_id INTEGER, " +
                    "shop_name TEXT, " +
                    "rented_cycle_count INTEGER, " +
                    "rented_date_time INTEGER, " +
                    "status TEXT, " +
                    "payment_per_hour INTEGER, " +
                    "hour_count INTEGER DEFAULT 0, " +
                    "total_paid_amount INTEGER DEFAULT 0" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all tables for version 1
        db.execSQL(CREATE_TABLE_USER);  // Create user table
        db.execSQL(CREATE_TABLE_SHOP);  // Create location/shop table
        db.execSQL(CREATE_TABLE_RENTAL); // Create rental table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Since this is version 1, there are no upgrades to handle.
        // You can leave this method empty or remove it entirely if not needed.
    }

    // ------------------------ USER METHODS ------------------------

    // Insert user
    public long insertUser(String username, String email, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_MOBILE, mobile);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result;
    }

    // Check if a user exists by email or phone number
    public boolean checkIfUserExists(String email, String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_EMAIL + " = ?",
                new String[]{email}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        cursor = db.query(TABLE_USER, null, COLUMN_MOBILE + " = ?",
                new String[]{mobile}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    // Check user credentials
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { email, password });

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // Get user details by email
    public Cursor getUserDetailsByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{email});
    }

    // ------------------------ LOCATION METHODS ------------------------

    // Add location/shop
    public void addLocation(LocationCard location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOP_NAME, location.getShopName());
        values.put(COLUMN_LOCATION, location.getLocation());
        values.put(COLUMN_RATING, location.getRating());
        values.put(COLUMN_BICYCLE_COUNT, location.getBicycleCount());
        values.put(COLUMN_IMAGE_URL, location.getImageUrl());
        values.put(COLUMN_LATITUDE, location.getLatLng().latitude);
        values.put(COLUMN_LONGITUDE, location.getLatLng().longitude);
        values.put(COLUMN_PRICE_PER_HOUR, location.getPricePerHour());

        long result = db.insert(TABLE_SHOP, null, values);
        if (result == -1) {
            Log.e("Database", "Error inserting data.");
        } else {
            Log.d("Database", "Data inserted successfully.");
        }
        db.close();
    }

    // Get all locations
    public List<LocationCard> getAllLocations() {
        List<LocationCard> locationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SHOP;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int shopNameIndex = cursor.getColumnIndex(COLUMN_SHOP_NAME);
                int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                int ratingIndex = cursor.getColumnIndex(COLUMN_RATING);
                int bicycleCountIndex = cursor.getColumnIndex(COLUMN_BICYCLE_COUNT);
                int imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);
                int latitudeIndex = cursor.getColumnIndex(COLUMN_LATITUDE);
                int longitudeIndex = cursor.getColumnIndex(COLUMN_LONGITUDE);
                int pricePerHourIndex = cursor.getColumnIndex(COLUMN_PRICE_PER_HOUR);

                if (idIndex >= 0 && shopNameIndex >= 0 && locationIndex >= 0 && ratingIndex >= 0 &&
                        bicycleCountIndex >= 0 && imageUrlIndex >= 0 && latitudeIndex >= 0 && longitudeIndex >= 0 &&
                        pricePerHourIndex >= 0) {

                    LocationCard location = new LocationCard(
                            cursor.getInt(idIndex),
                            cursor.getString(shopNameIndex),
                            cursor.getString(locationIndex),
                            cursor.getString(ratingIndex),
                            cursor.getInt(bicycleCountIndex),
                            cursor.getString(imageUrlIndex),
                            new LatLng(cursor.getDouble(latitudeIndex), cursor.getDouble(longitudeIndex)),
                            cursor.getInt(pricePerHourIndex)
                    );
                    locationList.add(location);
                } else {
                    Log.e("Database", "One or more columns are missing in the result set.");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return locationList;
    }

    // ------------------------ RENTAL METHODS ------------------------

    // Insert rental data
    public void insertRental(long userId, int shopId, String shopName, int rentedCycleCount,
                             long rentedDateTime, String status, int paymentPerHour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("shop_id", shopId);
        values.put("shop_name", shopName);
        values.put("rented_cycle_count", rentedCycleCount);
        values.put("rented_date_time", rentedDateTime);
        values.put("status", status);
        values.put("payment_per_hour", paymentPerHour);
        values.put("hour_count", 0);
        values.put("total_paid_amount", 0);

        db.insert(TABLE_RENTAL, null, values);
        db.close();
    }

    // Update rental amount and time
    public void updateRentalAmountAndTime(int rentalId, int hourCount, int totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hour_count", hourCount);
        values.put("total_paid_amount", totalAmount);

        db.update(TABLE_RENTAL, values, "id = ?", new String[]{String.valueOf(rentalId)});
        db.close();
    }
}
