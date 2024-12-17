package com.example.nailsalonmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MakeAppointment extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, dateEditText, startTimeEditText, endTimeEditText;
    private CheckBox nailArtCheckBox, pedicureCheckBox, manicureCheckBox;
    private Button makeAppointmentButton;

    // Local storage for appointments
    private static ArrayList<Appointment> appointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_appointment);

        // Initialize UI components
        nameEditText = findViewById(R.id.Name);
        phoneEditText = findViewById(R.id.NoHandphone);
        dateEditText = findViewById(R.id.Date);
        startTimeEditText = findViewById(R.id.StartTime);
        endTimeEditText = findViewById(R.id.EndTime);
        nailArtCheckBox = findViewById(R.id.NailArt);
        pedicureCheckBox = findViewById(R.id.Pedicure);
        manicureCheckBox = findViewById(R.id.Manicure);
        makeAppointmentButton = findViewById(R.id.button);

        // Open date and time pickers
        dateEditText.setOnClickListener(v -> openDatePicker());

        startTimeEditText.setOnClickListener(v -> {
            if (validateServiceSelection()) {
                openTimePicker();
            }
        });

        // Automatically calculate end time when start time is changed
        startTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAndSetEndTime();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Reset the start time when services are changed
        nailArtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> resetStartTime());
        pedicureCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> resetStartTime());
        manicureCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> resetStartTime());

        // Validate fields and save appointment
        makeAppointmentButton.setOnClickListener(v -> {
            if (validateFields()) {
                saveAppointment();
            }
        });
    }

    private boolean validateServiceSelection() {
        if (!nailArtCheckBox.isChecked() && !pedicureCheckBox.isChecked() && !manicureCheckBox.isChecked()) {
            Toast.makeText(this, "Please select at least one service before choosing the start time!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            dateEditText.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void openTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            if (validateSelectedTime(selectedHour, selectedMinute)) {
                String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                startTimeEditText.setText(formattedTime);
                calculateAndSetEndTime();
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private boolean validateSelectedTime(int selectedHour, int selectedMinute) {
        int totalTime = getTotalServiceTime();
        int latestStartTimeMinutes = (20 * 60) - totalTime;
        int latestStartHour = latestStartTimeMinutes / 60;
        int latestStartMinute = latestStartTimeMinutes % 60;

        if (selectedHour < 10 || selectedHour > latestStartHour ||
                (selectedHour == latestStartHour && selectedMinute > latestStartMinute)) {
            String latestStartTime = String.format("%02d:%02d", latestStartHour, latestStartMinute);
            Toast.makeText(this, "Start time must be between 10:00 AM and " + latestStartTime + "!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getTotalServiceTime() {
        int totalTime = 0;
        if (nailArtCheckBox.isChecked()) totalTime += 60;
        if (pedicureCheckBox.isChecked()) totalTime += 30;
        if (manicureCheckBox.isChecked()) totalTime += 30;
        return totalTime;
    }

    private void calculateAndSetEndTime() {
        String startTime = startTimeEditText.getText().toString().trim();
        if (startTime.isEmpty()) return;

        try {
            String[] parts = startTime.split(":");
            int startHour = Integer.parseInt(parts[0]);
            int startMinute = Integer.parseInt(parts[1]);
            int totalTime = getTotalServiceTime();

            int totalMinutes = startHour * 60 + startMinute + totalTime;
            int endHour = totalMinutes / 60;
            int endMinute = totalMinutes % 60;

            if (endHour > 20 || (endHour == 20 && endMinute > 0)) {
                Toast.makeText(this, "Appointment must finish by 8:00 PM!", Toast.LENGTH_SHORT).show();
                endTimeEditText.setText("");
                return;
            }

            String formattedEndTime = String.format("%02d:%02d", endHour, endMinute);
            endTimeEditText.setText(formattedEndTime);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid start time format!", Toast.LENGTH_SHORT).show();
            endTimeEditText.setText("");
        }
    }

    private void resetStartTime() {
        // Reset the start time if services change
        startTimeEditText.setText("");
        endTimeEditText.setText(""); // Clear end time as well
    }

    private boolean validateFields() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            Toast.makeText(this, "Name must be at least 3 characters long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty() || phone.length() < 12 || !phone.matches("\\d+")) {
            Toast.makeText(this, "Phone number must be at least 12 digits and contain only numbers!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTime.isEmpty()) {
            Toast.makeText(this, "Please select a start time!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!nailArtCheckBox.isChecked() && !pedicureCheckBox.isChecked() && !manicureCheckBox.isChecked()) {
            Toast.makeText(this, "Please select at least one service!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveAppointment() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please ensure the start and end times are valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Concatenate selected services
        StringBuilder services = new StringBuilder();
        if (nailArtCheckBox.isChecked()) services.append("Nail Art,");
        if (pedicureCheckBox.isChecked()) services.append("Pedicure,");
        if (manicureCheckBox.isChecked()) services.append("Manicure,");

        // Create a new Appointment object with all required details
        int appointmentNumber = appointments.size() + 1; // Increment number
        Appointment newAppointment = new Appointment(name, phone, services.toString().trim(), date, startTime, endTime, appointmentNumber);

        // Check for conflicts with existing appointments
        if (isTimeSlotAvailable(newAppointment)) {
            appointments.add(newAppointment);  // Save appointment to the list
            Toast.makeText(this, "Appointment saved successfully!", Toast.LENGTH_SHORT).show();

            // Clear input fields after saving
//            clearInputs();
        } else {
            Toast.makeText(this, "Appointment time conflicts with an existing appointment!", Toast.LENGTH_LONG).show();
        }
    }

    private void clearInputs() {
        // Clear all EditText fields
        nameEditText.setText("");
        phoneEditText.setText("");
        dateEditText.setText("");
        startTimeEditText.setText("");
        endTimeEditText.setText("");

        // Uncheck all CheckBoxes
        nailArtCheckBox.setChecked(false);
        pedicureCheckBox.setChecked(false);
        manicureCheckBox.setChecked(false);
    }

    // Method to check if the time slot is available
    private boolean isTimeSlotAvailable(Appointment newAppointment) {
        for (Appointment existingAppointment : appointments) {
            if (existingAppointment.getDate().equals(newAppointment.getDate())) {
                // Check if the new appointment overlaps with the existing appointment
                if (isTimeOverlap(
                        newAppointment.getStartTime(), newAppointment.getEndTime(),
                        existingAppointment.getStartTime(), existingAppointment.getEndTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check if two time slots overlap
    private boolean isTimeOverlap(String start1, String end1, String start2, String end2) {
        try {
            int start1Minutes = convertTimeToMinutes(start1);
            int end1Minutes = convertTimeToMinutes(end1);
            int start2Minutes = convertTimeToMinutes(start2);
            int end2Minutes = convertTimeToMinutes(end2);

            return (start1Minutes < end2Minutes && end1Minutes > start2Minutes);
        } catch (Exception e) {
            return false;
        }
    }

    // Convert time to minutes for easier comparison
    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }
}