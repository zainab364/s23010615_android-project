
package com.s23010615.zainab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextLoginUsername);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        loginButton = findViewById(R.id.LoginButton);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < 3) {
                Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertData(username, password);
            if (inserted) {
                Toast.makeText(this, "User & Password saved to DB", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
