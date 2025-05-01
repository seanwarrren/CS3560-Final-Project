package com.budgetapp;

public class UserManager {
    private static String currentUser = null;

    public static void createUser(String name) {
        currentUser = name;
        System.out.println("User created: " + name);
    }

    public static void deleteUser() {
        System.out.println("User deleted: " + currentUser);
        currentUser = null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
