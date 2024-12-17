package com.example.nailsalonmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewAppointmentHistory extends AppCompatActivity {

    private TableLayout appointmentHistoryTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_appointment_history);

        // Find the AppointmentHistoryTable
        appointmentHistoryTable = findViewById(R.id.AppointmentTable);
    }

    private void displayHistoryAppointments(ArrayList<Appointment> historyAppointments) {
        // Clear the table before adding new rows but keep the header
        appointmentHistoryTable.removeViews(1, appointmentHistoryTable.getChildCount() - 1);

        // Add rows dynamically for each appointment in the history
        for (Appointment appointment : historyAppointments) {
            addHistoryAppointmentRow(appointment);
        }
    }

    private void addHistoryAppointmentRow(Appointment appointment) {
        TableRow row = new TableRow(this);

        // Set layout parameters for the row
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, // Match parent width
                TableRow.LayoutParams.WRAP_CONTENT  // Wrap content height
        );
        row.setLayoutParams(rowParams);
        row.setBackgroundColor(Color.WHITE); // Set row background to white

        // Common layout parameters for all TextViews
        TableRow.LayoutParams textParams = new TableRow.LayoutParams(
                1, // Layout width set to 1dp for weight distribution
                TableRow.LayoutParams.WRAP_CONTENT, // Wrap content for height
                1 // Weight for equal column distribution
        );

        // Create and configure Name TextView
        TextView name = new TextView(this);
        name.setLayoutParams(textParams);
        name.setText(appointment.getName());
        name.setGravity(Gravity.CENTER);
        name.setPadding(10, 10, 10, 10);
        name.setTextColor(Color.parseColor("#eb7e8a")); // Pink color
        row.addView(name);

        // Create and configure Phone TextView
        TextView phone = new TextView(this);
        phone.setLayoutParams(textParams);
        phone.setText(appointment.getPhone());
        phone.setGravity(Gravity.CENTER);
        phone.setPadding(10, 10, 10, 10);
        phone.setTextColor(Color.parseColor("#eb7e8a"));
        row.addView(phone);

        // Create and configure Services TextView
        TextView services = new TextView(this);
        services.setLayoutParams(textParams);

        // Combine services with line breaks
        StringBuilder servicesText = new StringBuilder();
        for (String service : appointment.getServices().split(",")) {
            if (!service.isEmpty()) {
                servicesText.append(service.trim()).append("\n");
            }
        }
        services.setText(servicesText.toString().trim()); // Remove trailing newline
        services.setGravity(Gravity.CENTER);
        services.setPadding(10, 10, 10, 10);
        services.setTextColor(Color.parseColor("#eb7e8a"));
        row.addView(services);

        // Create and configure Date TextView
        TextView date = new TextView(this);
        date.setLayoutParams(textParams);
        date.setText(appointment.getDate());
        date.setGravity(Gravity.CENTER);
        date.setPadding(10, 10, 10, 10);
        date.setTextColor(Color.parseColor("#eb7e8a"));
        row.addView(date);

        // Create and configure Time TextView
        TextView time = new TextView(this);
        time.setLayoutParams(textParams);
        time.setText(appointment.getStartTime() + " - " + appointment.getEndTime());
        time.setGravity(Gravity.CENTER);
        time.setPadding(10, 10, 10, 10);
        time.setTextColor(Color.parseColor("#eb7e8a"));
        row.addView(time);

        // Add the row to the TableLayout
        appointmentHistoryTable.addView(row);
    }
}
