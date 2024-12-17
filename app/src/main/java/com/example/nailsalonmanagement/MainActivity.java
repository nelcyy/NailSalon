package com.example.nailsalonmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Find the ViewAppointment button
        Button makeAppointmentButton = findViewById(R.id.MakeAppointment);
        Button viewAppointmentButton = findViewById(R.id.ViewAppointment);
        Button viewAppointmentHistoryButton = findViewById(R.id.ViewAppointmentHistory);

        // Set a click listener
        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeAppointment.class);
                startActivity(intent);
            }
        });

        viewAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAppointment.class);
                startActivity(intent);
            }
        });

        viewAppointmentHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAppointmentHistory.class);
                startActivity(intent);
            }
        });
    }
}