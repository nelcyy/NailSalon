package com.example.nailsalonmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Stack;

public class ViewAppointment extends AppCompatActivity {

    private TableLayout appointmentTable;
    private static ArrayList<Appointment> appointmentsArray; // Array for storing data
    private static ArrayList<Appointment> historyAppointment; // Array for storing history of deleted appointments
    private static Stack<Appointment> undoStack, redoStack;
    private LinearLayout undoRedoLayout;  // Reference to the Undo/Redo buttons layout
    private Button undoButton, redoButton; // Undo and Redo buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_appointment);

        // Find the AppointmentTable
        appointmentTable = findViewById(R.id.AppointmentTable);

        // Initialize the list to track appointments
        appointmentsArray = new ArrayList<>();
        historyAppointment = new ArrayList<>();  // Initialize historyAppointment array

        undoStack = new Stack<>();
        redoStack = new Stack<>();

        // Set Undo and Redo button listeners
        undoButton = findViewById(R.id.Undo);
        redoButton = findViewById(R.id.Redo);
        undoRedoLayout = findViewById(R.id.UndoRedoLayout);

        undoButton.setOnClickListener(v -> undoDelete());
        redoButton.setOnClickListener(v -> redoDelete());

        // Retrieve appointments from the static array in MakeAppointment
        ArrayList<Appointment> appointments = MakeAppointment.getAppointments();
        for (Appointment appointment : appointments) {
            appointmentsArray.add(appointment); // Store in ArrayList
        }

        // Display appointments directly from appointmentsArray
        displayAppointments();
    }

    // Method to get the static historyAppointment
    public static ArrayList<Appointment> getHistoryAppointments() {
        return historyAppointment;
    }

    private void displayAppointments() {
        // Clear the table before adding new rows but keep the header
        appointmentTable.removeViews(1, appointmentTable.getChildCount() - 1);

        // Add rows dynamically for each appointment in the ArrayList
        for (Appointment appointment : appointmentsArray) {
            addAppointmentRow(appointment);
        }

        // Adjust visibility of Undo/Redo buttons based on the stacks
        if (!undoStack.isEmpty() || !redoStack.isEmpty()) {
            undoRedoLayout.setVisibility(View.VISIBLE);
        } else {
            undoRedoLayout.setVisibility(View.GONE);
        }
    }

    private void addAppointmentRow(Appointment appointment) {
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

        // Set the row's click listener to delete the appointment
        row.setOnClickListener(v -> deleteAppointmentFromRow(row));

        // Add the row to the TableLayout
        appointmentTable.addView(row);
    }

    private void deleteAppointmentFromRow(TableRow row) {
        TextView nameView = (TextView) row.getChildAt(0); // Name column
        TextView phoneView = (TextView) row.getChildAt(1); // Phone column
        TextView dateView = (TextView) row.getChildAt(3); // Date column

        String name = nameView.getText().toString();
        String phone = phoneView.getText().toString();
        String date = dateView.getText().toString();

        // Find the appointment in the appointmentsArray based on multiple criteria
        Appointment appointmentToRemove = null;
        for (Appointment appointment : appointmentsArray) {
            if (appointment.getName().equals(name) &&
                    appointment.getPhone().equals(phone) &&
                    appointment.getDate().equals(date)) {
                appointmentToRemove = appointment;
                break;
            }
        }

        if (appointmentToRemove != null) {
            // Remove from appointmentsArray
            appointmentsArray.remove(appointmentToRemove);

            // Also remove from the source of appointments (MakeAppointment.getAppointments())
            ArrayList<Appointment> appointments = MakeAppointment.getAppointments();
            appointments.remove(appointmentToRemove);

            // Add the deleted appointment to historyAppointment array (to keep track of deleted appointments)
            historyAppointment.add(appointmentToRemove);

            // Remove the row from TableLayout
            appointmentTable.removeView(row);

            // Refresh the table
            displayAppointments();

            // Push the deleted appointment onto the undo stack
            undoStack.push(appointmentToRemove);

            // Enable Undo/Redo buttons visibility
            undoRedoLayout.setVisibility(View.VISIBLE);
        }
    }

    private void undoDelete() {
        if (!undoStack.isEmpty()) {
            // Get the last deleted appointment from the undo stack
            Appointment appointmentToUndo = undoStack.pop();

            // Check if the appointment is already in the appointmentsArray before adding it back
            if (!appointmentsArray.contains(appointmentToUndo)) {
                // Add it back to the appointmentsArray
                appointmentsArray.add(appointmentToUndo);

                // Also add it back to MakeAppointment.getAppointments() if not already there
                ArrayList<Appointment> appointments = MakeAppointment.getAppointments();
                if (!appointments.contains(appointmentToUndo)) {
                    appointments.add(appointmentToUndo);
                }

                // Refresh the table
                displayAppointments();

                // Push this appointment onto the redo stack in case we need to redo the deletion
                redoStack.push(appointmentToUndo);
            }
        }
    }

    private void redoDelete() {
        if (!redoStack.isEmpty()) {
            // Get the last undone appointment from the redo stack
            Appointment appointmentToRedo = redoStack.pop();

            // Remove it again from the appointmentsArray (redo the deletion)
            appointmentsArray.remove(appointmentToRedo);

            // Also remove it from MakeAppointment.getAppointments()
            ArrayList<Appointment> appointments = MakeAppointment.getAppointments();
            appointments.remove(appointmentToRedo);

            // Refresh the table
            displayAppointments();

            // Push this appointment onto the undo stack in case we want to undo the redo
            undoStack.push(appointmentToRedo);
        }
    }
}

