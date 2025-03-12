package lk.nd.cycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.nd.cycler.database.DatabaseHelper;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

import java.io.Serializable;
import java.util.ArrayList;

import lk.nd.cycler.model.OngoingRental;

public class PayNowActivity extends AppCompatActivity {

    TextView shop, bcount, hourCount, perHour, payAmount;
    Button payNowBtn;
    private ActivityResultLauncher<Intent> payHereLauncher;
    long total;
    long userId;
    int locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_now);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        userId = sharedPreferences.getLong("user_id", -1); // Default -1 if not found
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String mobile = sharedPreferences.getString("mobile", "");

        shop = findViewById(R.id.shop);
        bcount = findViewById(R.id.bcount);
        hourCount = findViewById(R.id.hourCount);
        perHour = findViewById(R.id.perHour);
        payAmount = findViewById(R.id.payAmount);
        payNowBtn = findViewById(R.id.payNowBtn);

        Intent intent = getIntent();

        if (intent != null) {
            OngoingRental rental = (OngoingRental) intent.getSerializableExtra("rentalData");
            total = intent.getLongExtra("total", 0);

            if (rental != null) {
                shop.setText(rental.getShopName());
                bcount.setText(rental.getRentedCycleCount() + " Bicycles");
                hourCount.setText(rental.getTimeSpentInHours() + " hours");
                perHour.setText("Rs. " + rental.getPaymentPerHour() + " per hour");
                payAmount.setText("Total: Rs. " + total + ".00");
                locationId = rental.getLocationId();
            }
        }

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitRequest req = new InitRequest();
                req.setMerchantId(Constants.MERCHANT_ID);
                req.setMerchantSecret(Constants.MERCHANT_SECRET);
                req.setCurrency("LKR");
                req.setAmount(total);

                String orderId = String.valueOf(System.currentTimeMillis());

                req.setOrderId(orderId);
                req.setItemsDescription("Rental Payment");
                req.setCustom1("Custom message 1");
                req.setCustom2("Custom message 2");

                // Customer Details
                req.getCustomer().setFirstName(username != null ? username : "Guest");
                req.getCustomer().setLastName("N/A"); // Avoid empty last name
                req.getCustomer().setEmail(email != null ? email : "noemail@example.com");
                req.getCustomer().setPhone(mobile != null ? mobile : "0000000000");


                req.getCustomer().getAddress().setAddress("No.2, Kandy Road");
                req.getCustomer().getAddress().setCity("Kandy");
                req.getCustomer().getAddress().setCountry("Sri Lanka");
                Log.d("PayHere", "username: "+ username+ " email: "+ email+ " contact: "+ mobile+ " Total: "+total+ " OrderId: " +orderId+ " Address: No.2, Kandy Road, city: Kandy");

                // Delivery Address (same as billing in this case)
                req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
                req.getCustomer().getDeliveryAddress().setCity("Kandy");
                req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");

                // Start Payment Activity
                Intent intent = new Intent(PayNowActivity.this, PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

                payHereLauncher.launch(intent);
            }
        });

        payHereLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
                            Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                            if (serializable instanceof PHResponse) {
                                PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;
                                String msg = response.isSuccess() ? "Payment Success. " + response.getData() : "Payment failed: " + response;
                                Log.d("PAYHERE", msg);


                                if (response.isSuccess()) {
                                    if (intent != null) {
                                        OngoingRental rental = (OngoingRental) intent.getSerializableExtra("rentalData");
                                        if (rental != null) {
                                            int rentalId = rental.getId();
                                            int hours = (int) rental.getTimeSpentInHours();

                                            DatabaseHelper dbHelper = new DatabaseHelper(this);
                                            dbHelper.updateRentalStatusAndPayment(rentalId, hours, (int) total);

                                            // Update bicycle count in the shop table
                                            dbHelper.updateBicycleCount(locationId, rental.getRentedCycleCount());

                                            Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();

                                            finish();
                                        }
                                    }
                                } else {

                                    Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );


    }
}