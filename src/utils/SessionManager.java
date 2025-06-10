package utils;

import user.model.Admin;
import user.model.Landlord;
import user.model.Tenant;
import user.model.User;

import java.io.*;

public class SessionManager {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Landlord setLandlordData() {
        if (currentUser instanceof Landlord) {
            return new Landlord(currentUser);
        } else {
            System.err.println("Current user is not a Landlord.");
            return null;
        }
    }

    public static Tenant setTenantData() {
        if (currentUser instanceof Tenant) {
            return new Tenant(currentUser);
        } else {
            System.err.println("Current user is not a Tenant.");
            return null;
        }
    }

    public static boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
