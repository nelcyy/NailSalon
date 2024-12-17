package com.example.nailsalonmanagement;

import java.util.ArrayList;

public class BST {

    // BST Node class
    private class BSTNode {
        Appointment appointment;
        BSTNode left, right;

        public BSTNode(Appointment appointment) {
            this.appointment = appointment;
            this.left = this.right = null;
        }
    }

    private BSTNode root;

    public BST() {
        root = null;
    }

    // Insert an appointment into the BST
    public void insert(Appointment appointment) {
        root = insertRec(root, appointment);
    }

    private BSTNode insertRec(BSTNode root, Appointment appointment) {
        if (root == null) {
            root = new BSTNode(appointment);
            return root;
        }

        // Compare dates to determine left or right placement
        if (appointment.getDate().compareTo(root.appointment.getDate()) < 0) {
            root.left = insertRec(root.left, appointment);
        } else {
            root.right = insertRec(root.right, appointment);
        }

        return root;
    }

    // Inorder traversal (Sorted by date)
    public void inorder(ArrayList<Appointment> appointmentsList) {
        inorderRec(root, appointmentsList);
    }

    private void inorderRec(BSTNode root, ArrayList<Appointment> appointmentsList) {
        if (root != null) {
            inorderRec(root.left, appointmentsList);
            appointmentsList.add(root.appointment);
            inorderRec(root.right, appointmentsList);
        }
    }

    // Reverse inorder traversal (Sorted by newest - for ByNewest option)
    public void reverseInorder(ArrayList<Appointment> appointmentsList) {
        reverseInorderRec(root, appointmentsList);
    }

    private void reverseInorderRec(BSTNode root, ArrayList<Appointment> appointmentsList) {
        if (root != null) {
            reverseInorderRec(root.right, appointmentsList);
            appointmentsList.add(root.appointment);
            reverseInorderRec(root.left, appointmentsList);
        }
    }

    // Remove an appointment from the BST
    public void remove(Appointment appointment) {
        root = removeRec(root, appointment);
    }

    private BSTNode removeRec(BSTNode root, Appointment appointment) {
        if (root == null) {
            return root;
        }

        // Compare using the appointment fields (not just the date)
        int compareResult = compareAppointments(appointment, root.appointment);

        // If appointment to be deleted is smaller (go left)
        if (compareResult < 0) {
            root.left = removeRec(root.left, appointment);
        }
        // If appointment to be deleted is larger (go right)
        else if (compareResult > 0) {
            root.right = removeRec(root.right, appointment);
        }
        // Appointment found
        else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Node with two children, get the inorder successor (smallest in the right subtree)
            root.appointment = minValue(root.right);

            // Delete the inorder successor
            root.right = removeRec(root.right, root.appointment);
        }

        return root;
    }

    // Helper method to compare appointments (using multiple fields)
    private int compareAppointments(Appointment a1, Appointment a2) {
        int nameComparison = a1.getName().compareTo(a2.getName());
        if (nameComparison != 0) return nameComparison;

        int phoneComparison = a1.getPhone().compareTo(a2.getPhone());
        if (phoneComparison != 0) return phoneComparison;

        return a1.getDate().compareTo(a2.getDate());  // Compare dates last
    }

    // Helper method to find the minimum value node (used for BST remove operation)
    private Appointment minValue(BSTNode root) {
        Appointment minValue = root.appointment;
        while (root.left != null) {
            minValue = root.left.appointment;
            root = root.left;
        }
        return minValue;
    }

    // Clears the entire BST by setting the root to null
    public void clear() {
        root = null;
    }
}
