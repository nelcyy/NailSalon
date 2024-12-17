package com.example.nailsalonmanagement;

import java.util.ArrayList;

class BST {
    private BSTNode root;
    private ArrayList<Appointment> addedOrderList; // Keeps track of added order

    private static class BSTNode {
        Appointment data;
        BSTNode left, right;

        public BSTNode(Appointment data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public BST() {
        root = null;
        addedOrderList = new ArrayList<>();
    }

    private boolean isDuplicate(Appointment newAppointment) {
        for (Appointment appointment : addedOrderList) {
            if (appointment.getDate().equals(newAppointment.getDate()) &&
                    appointment.getName().equals(newAppointment.getName())) {
                return true;
            }
        }
        return false;
    }

    public void insert(Appointment appointment) {
        if (isDuplicate(appointment)) {
            System.out.println("Duplicate appointment not allowed!");
            return;
        }
        root = insertRec(root, appointment);
        addedOrderList.add(appointment);
    }

    private BSTNode insertRec(BSTNode node, Appointment appointment) {
        if (node == null) {
            return new BSTNode(appointment);
        }

        if (appointment.getDate().compareTo(node.data.getDate()) < 0) {
            node.left = insertRec(node.left, appointment);
        } else if (appointment.getDate().compareTo(node.data.getDate()) > 0) {
            node.right = insertRec(node.right, appointment);
        }
        return node;
    }

    // Delete appointment
    public void delete(Appointment appointment) {
        root = deleteRec(root, appointment);
        addedOrderList.remove(appointment); // Remove from added order list
    }

    private BSTNode deleteRec(BSTNode node, Appointment appointment) {
        if (node == null) return null;

        if (appointment.getDate().equals(node.data.getDate()) &&
                appointment.getName().equals(node.data.getName())) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            node.data = findMin(node.right);
            node.right = deleteRec(node.right, node.data);
        } else if (appointment.getDate().compareTo(node.data.getDate()) < 0) {
            node.left = deleteRec(node.left, appointment);
        } else {
            node.right = deleteRec(node.right, appointment);
        }
        return node;
    }

    private Appointment findMin(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    // Display appointments sorted by date
    public void displayInOrder() {
        System.out.println("Appointments Sorted by Date:");
        inorderRec(root);
    }

    private void inorderRec(BSTNode node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.println(node.data);
            inorderRec(node.right);
        }
    }

    // Display appointments in added order
    public void displayAddedOrder() {
        System.out.println("Appointments in Added Order:");
        for (Appointment appointment : addedOrderList) {
            System.out.println(appointment);
        }
    }
}