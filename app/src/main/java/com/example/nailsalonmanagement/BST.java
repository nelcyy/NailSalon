package com.example.nailsalonmanagement;

import java.util.ArrayList;

public class BST {
    private static BST instance;
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

    // Constructor
    public BST() {
        root = null;
        addedOrderList = new ArrayList<>();
    }

    // Static method to get the singleton instance of BST
    public static BST getInstance() {
        if (instance == null) {
            instance = new BST();
        }
        return instance;
    }

    // Interface for the visitor
    public interface AppointmentVisitor {
        void visit(Appointment appointment);
    }

    // Check for duplicate appointments
    private boolean isDuplicate(Appointment newAppointment) {
        for (Appointment appointment : addedOrderList) {
            if (appointment.getDate().equals(newAppointment.getDate()) &&
                    appointment.getName().equals(newAppointment.getName())) {
                return true;
            }
        }
        return false;
    }

    // Insert appointment into BST
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

    // Delete appointment from BST
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

    // Find the minimum appointment in the tree (used for deleting nodes with two children)
    private Appointment findMin(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    // In-order traversal to display appointments sorted by date
    public void inOrderTraversal(AppointmentVisitor visitor) {
        inOrderTraversalRec(root, visitor);
    }

    private void inOrderTraversalRec(BSTNode node, AppointmentVisitor visitor) {
        if (node != null) {
            inOrderTraversalRec(node.left, visitor);  // Traverse left
            visitor.visit(node.data);  // Visit current node
            inOrderTraversalRec(node.right, visitor);  // Traverse right
        }
    }

    // Display appointments sorted by date (old method, now using inOrderTraversal)
    public void displayInOrder() {
        System.out.println("Appointments Sorted by Date:");
        inOrderTraversal(new AppointmentVisitor() {
            @Override
            public void visit(Appointment appointment) {
                System.out.println(appointment);
            }
        });
    }

    // Display appointments in added order
    public void displayAddedOrder() {
        System.out.println("Appointments in Added Order:");
        for (Appointment appointment : addedOrderList) {
            System.out.println(appointment);
        }
    }
}
