package com.example.nailsalonmanagement;

public class Appointment {
    private String name;
    private String phone;
    private String services;
    private String date;   // Used for comparison in BST
    private String startTime;
    private String endTime;
    private int number;    // Order of addition (1, 2, 3...)

    public Appointment(String name, String phone, String services, String date, String startTime, String endTime, int number) {
        this.name = name;
        this.phone = phone;
        this.services = services;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
    }

    // Getters
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getServices() { return services; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public int getNumber() { return number; }

    @Override
    public String toString() {
        return number + ". " + name + " | " + phone + " | " + services + " | " + date + " | " + startTime + " - " + endTime;
    }
}