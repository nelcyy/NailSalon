package com.example.nailsalonmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAppointmentHistory extends AppCompatActivity {

    private TableLayout appointmentTable;
    private RadioGroup sortingRadioGroup;
    private RadioButton newestRadio, dateRadio;

    private static BST historyBST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_appointment_history);

        // Initialize views
        appointmentTable = findViewById(R.id.AppointmentTable);
        sortingRadioGroup = findViewById(R.id.radioGroup);
        newestRadio = findViewById(R.id.NewestRadio);
        dateRadio = findViewById(R.id.DateRadio);

        // Initialize BST
        historyBST = BST.getInstance();

        // Display the default order (Newest)
        displayNewestOrder();

        // Set up radio button listener
        sortingRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.NewestRadio) {
                displayNewestOrder();
            } else if (checkedId == R.id.DateRadio) {
                displaySortedByDate();
            }
        });
    }

    // Method to display appointments in added (newest) order
    private void displayNewestOrder() {
        clearTable();
        for (Appointment appointment : historyBST.getAddedOrderList()) {
            addAppointmentRow(appointment);
        }
    }

    // Method to display appointments sorted by date (in-order traversal)
    private void displaySortedByDate() {
        clearTable();
        historyBST.inOrderTraversal(appointment -> addAppointmentRow(appointment));
    }

    // Clears the table except for the header
    private void clearTable() {
        appointmentTable.removeViews(1, appointmentTable.getChildCount() - 1);
    }

    // Dynamically add rows to the table
    private void addAppointmentRow(Appointment appointment) {
        TableRow row = new TableRow(this);

        // Row parameters
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(rowParams);
        row.setBackgroundColor(Color.WHITE);

        // Common layout parameters for TextViews
        TableRow.LayoutParams textParams = new TableRow.LayoutParams(
                1, TableRow.LayoutParams.WRAP_CONTENT, 1
        );

        // Name
        row.addView(createTextView(appointment.getName(), textParams));
        // Phone
        row.addView(createTextView(appointment.getPhone(), textParams));
        // Services
        row.addView(createTextView(appointment.getServices().replace(",", "\n"), textParams));
        // Date
        row.addView(createTextView(appointment.getDate(), textParams));
        // Time
        row.addView(createTextView(appointment.getStartTime() + " - " + appointment.getEndTime(), textParams));

        // Add row to table
        appointmentTable.addView(row);
    }

    // Helper method to create TextView
    private TextView createTextView(String text, TableRow.LayoutParams params) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.parseColor("#eb7e8a"));
        return textView;
    }
}
