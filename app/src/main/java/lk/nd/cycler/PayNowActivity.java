package lk.nd.cycler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.nd.cycler.model.OngoingRental;

public class PayNowActivity extends AppCompatActivity {

    TextView shop, bcount, hourCount, perHour, payAmount;
    Button payNowBtn;

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

        shop = findViewById(R.id.shop);
        bcount = findViewById(R.id.bcount);
        hourCount = findViewById(R.id.hourCount);
        perHour = findViewById(R.id.perHour);
        payAmount = findViewById(R.id.payAmount);
        payNowBtn = findViewById(R.id.payNowBtn);

        Intent intent = getIntent();

        if (intent != null) {
            OngoingRental rental = (OngoingRental) intent.getSerializableExtra("rentalData");
            long total = intent.getLongExtra("total", 0);

            if (rental != null) {
                shop.setText(rental.getShopName());
                bcount.setText(rental.getRentedCycleCount() + " Bicycles");
                hourCount.setText(rental.getTimeSpentInHours() + " hours");
                perHour.setText("Rs. " + rental.getPaymentPerHour() + " per hour");
                payAmount.setText("Total: Rs. " + total + ".00");
            }
        }

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}