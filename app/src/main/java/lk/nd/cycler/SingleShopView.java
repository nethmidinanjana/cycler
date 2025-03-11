package lk.nd.cycler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import lk.nd.cycler.database.DatabaseHelper;
import lk.nd.cycler.model.LocationCard;

public class SingleShopView extends AppCompatActivity {

    EditText qtyEditText;
    Button placeOrderBtn;
    long userId;
    LocationCard location;
    int locationId;
    TextView bicycleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_single_shop_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        location = getIntent().getParcelableExtra("location_data");
        locationId = location.getId();

        if (location != null) {
            // Example of setting data in UI
            TextView shopName = findViewById(R.id.shopNameTextView);
            TextView locationText = findViewById(R.id.locationTextView);
            TextView rating = findViewById(R.id.ratingTextView);
            bicycleCount = findViewById(R.id.bicycleCountTextView);
            ImageView shopImage = findViewById(R.id.shopImageView);

            shopName.setText(location.getShopName());
            locationText.setText(location.getLocation());
            rating.setText(location.getRating()+" Reviews");
            bicycleCount.setText(location.getBicycleCount()+ " Bicycles Available");

            Glide.with(this)
                    .load(location.getImageUrl())
                    .placeholder(R.mipmap.rental_place1)
                    .into(shopImage);
        }

        qtyEditText = findViewById(R.id.qtyEditText);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        userId = sharedPreferences.getLong("user_id", -1);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String mobile = sharedPreferences.getString("mobile", "");

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty = qtyEditText.getText().toString().trim();

                if(qty.isEmpty()){
                    Toast.makeText(SingleShopView.this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                } else{
                    int quantity = Integer.parseInt(qty);

                    if(quantity == 0){
                        Toast.makeText(SingleShopView.this, "Quantity can not be zero.", Toast.LENGTH_SHORT).show();
                    }else{
                        placeOrderBtn.setEnabled(false);

                        long rentedDateTime = System.currentTimeMillis();  // Set the current time
                        String status = "Pending";
                        int paymentPerHour = location.getPricePerHour();

                        DatabaseHelper dbHelper = new DatabaseHelper(SingleShopView.this);
                        dbHelper.insertRental(userId, locationId, location.getShopName(), quantity, rentedDateTime, status, paymentPerHour);

                        SQLiteDatabase db = null;

                        try {
                            db = openOrCreateDatabase("app_database", MODE_PRIVATE, null);
                            String updateQuery = "UPDATE shop SET bicycle_count = bicycle_count - ? WHERE id = ?";

                            SQLiteStatement statement = db.compileStatement(updateQuery);
                            statement.bindLong(1, quantity);
                            statement.bindLong(2, locationId);

                            int affectedRows = statement.executeUpdateDelete(); // Returns the number of affected rows

                            if (affectedRows > 0) {
                                Log.d("DB_UPDATE", "Bicycle count updated successfully");
                            } else {
                                Log.e("DB_UPDATE", "Failed to update bicycle count");
                            }
                        } catch (Exception e) {
                            Log.e("DB_ERROR", "Database error: " + e.getMessage());
                        } finally {
                            if (db != null && db.isOpen()) {
                                db.close();
                            }
                        }



                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);

                        bicycleCount.setText(location.getBicycleCount() - quantity+ " Bicycles Available");
                        qtyEditText.setText("");
                        Toast.makeText(SingleShopView.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                        placeOrderBtn.setEnabled(true);


                    }
                }
            }
        });


    }
}