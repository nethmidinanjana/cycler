package lk.nd.cycler;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignUpActivity extends AppCompatActivity {

    TextView signInNavigationLink;
    Button signUpBtn;
    EditText signupUsernameEditText, signupEmailEditText, signUpContactEditText, signUpPasswordEditText;
    private DatabaseHelper dbHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        signUpBtn = findViewById(R.id.signUpBtn);
        signupUsernameEditText = findViewById(R.id.signupUsernameEditText);
        signupEmailEditText = findViewById(R.id.signupEmailEditText);
        signUpContactEditText = findViewById(R.id.signUpContactEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);

        signInNavigationLink = findViewById(R.id.signUpNavigationLink);
        signInNavigationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("user_id", -1); // If no user_id, return -1

        if (userId != -1) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //signup
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do the signup
                String username = signupUsernameEditText.getText().toString();
                String email = signupEmailEditText.getText().toString();
                String mobile = signUpContactEditText.getText().toString();
                String password = signUpPasswordEditText.getText().toString();

                if(username.isBlank()){
                    Toast.makeText(SignUpActivity.this, "Username cannot be empty or just spaces!", Toast.LENGTH_LONG).show();
                }else if(email.isBlank()){
                    Toast.makeText(SignUpActivity.this, "Email cannot be empty or just spaces!", Toast.LENGTH_LONG).show();
                } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    Toast.makeText(SignUpActivity.this, "Invalid Email!", Toast.LENGTH_LONG).show();
                } else if(mobile.isBlank()){
                    Toast.makeText(SignUpActivity.this, "Mobile cannot be empty or just spaces!", Toast.LENGTH_LONG).show();
                } else if (!mobile.matches("^0[7-9]\\d{8}$")) {
                    Toast.makeText(SignUpActivity.this, "Invalid Mobile!", Toast.LENGTH_LONG).show();
                } else if (password.isBlank()) {
                    Toast.makeText(SignUpActivity.this, "Password cannot be empty or just spaces!", Toast.LENGTH_LONG).show();
                }else{
                    //check if the email or phone number already in the database
                    signUpBtn.setEnabled(false);
                    if(dbHelper.checkIfUserExists(email, mobile)){
                        Toast.makeText(SignUpActivity.this, "Email or Mobile number already in use!", Toast.LENGTH_LONG).show();
                        signUpBtn.setEnabled(true);
                    } else {
                        long result = dbHelper.insertUser(username, email, mobile, password);

                        if(result == -1){
                            Toast.makeText(SignUpActivity.this, "Sign up failed!", Toast.LENGTH_LONG).show();
                            signUpBtn.setEnabled(true);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up Success!", Toast.LENGTH_LONG).show();

                            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            // Save user information (User ID, Username, Email, Mobile, Password)
                            editor.putLong("user_id", result);
                            editor.putString("username", username);
                            editor.putString("email", email);
                            editor.putString("mobile", mobile);
                            editor.putString("password", password);
                            editor.apply();

                            //save to app storage and go to home
                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }


                }

            }
        });
    }
}