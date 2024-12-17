package com.example.nailsalonmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private static ArrayList<Regist.User> userList = Regist.userList; // Referencing the same user list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.sign_up_text);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            boolean isValid = false;

            // Check if the entered credentials match any registered user
            for (Regist.User user : userList) {
                if (user.email.equals(email) && user.password.equals(password)) {
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class)); // Navigate to MainActivity
            } else {
                Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
            }
        });

        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Regist.class)); // Navigate to Registration
        });
    }
}

