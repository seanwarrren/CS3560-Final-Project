package com.budgetapp;

// Manages the current user's session in the budgeting app
public class UserManager {
    
    // Hold name of currently logged in user
    private static String currentUser = null;

    // Creates a new user session
    public static void createUser(String name) {
        currentUser = name;
        System.out.println("User created: " + name);
    }

    // Deletes current user session and logs out
    public static void deleteUser() {
        System.out.println("User deleted: " + currentUser);
        currentUser = null;
    }

    // Gets the name of the currently logged-in user
    public static String getCurrentUser() {
        return currentUser;
    }

    // Checks whether a user is currently logged in
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
