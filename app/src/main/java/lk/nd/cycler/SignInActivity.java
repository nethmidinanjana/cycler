package lk.nd.cycler;

import static lk.nd.cycler.database.DatabaseHelper.COLUMN_EMAIL;
import static lk.nd.cycler.database.DatabaseHelper.COLUMN_MOBILE;
import static lk.nd.cycler.database.DatabaseHelper.COLUMN_PASSWORD;
import static lk.nd.cycler.database.DatabaseHelper.COLUMN_USERNAME;
import static lk.nd.cycler.database.DatabaseHelper.COLUMN_USER_ID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.nd.cycler.database.DatabaseHelper;

public class SignInActivity extends AppCompatActivity {
    TextView signUpNavigationLink;
    Button signInBtn;
    EditText signinEmailEditText, signInPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signInBtn = findViewById(R.id.signInBtn);
        signinEmailEditText = findViewById(R.id.signinEmailEditText);
        signInPasswordEditText = findViewById(R.id.signInPasswordEditText);

        signUpNavigationLink = findViewById(R.id.signUpNavigationLink);
        signUpNavigationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = signinEmailEditText.getText().toString();
                String password = signInPasswordEditText.getText().toString();

                if(email.isBlank()){
                    Toast.makeText(SignInActivity.this, "Email required.", Toast.LENGTH_SHORT).show();
                } else if (password.isBlank()) {
                    Toast.makeText(SignInActivity.this, "Password required.", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseHelper dbHelper = new DatabaseHelper(SignInActivity.this);

                    boolean isValid = dbHelper.checkUserCredentials(email, password);

                    if (isValid) {
                        // Get user details from the database
                        // Fetch user details based on the email
                        Cursor cursor = dbHelper.getUserDetailsByEmail(email);

                        if (cursor != null && cursor.moveToFirst()) {
                            // Ensure the column exists
                            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
                            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                            int mobileIndex = cursor.getColumnIndex(COLUMN_MOBILE);
                            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);

                            // Check if all columns exist in the cursor
                            if (userIdIndex != -1 && usernameIndex != -1 && emailIndex != -1 && mobileIndex != -1 && passwordIndex != -1) {
                                long userId = cursor.getLong(userIdIndex);
                                String username = cursor.getString(usernameIndex);
                                String mobile = cursor.getString(mobileIndex);

                                // Save user information in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putLong("user_id", userId);
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("mobile", mobile);
                                editor.putString("password", password);

                                editor.apply();

                                // Proceed to the MainActivity
                                Toast.makeText(SignInActivity.this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Error retrieving user details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignInActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                        }

                        cursor.close();

                    } else {
                        // Invalid credentials, show error message
                        Toast.makeText(SignInActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}