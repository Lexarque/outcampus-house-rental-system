package utils;

import user.model.Admin;
import user.model.Landlord;
import user.model.Tenant;
import user.model.User;

public class SessionManager {
    public static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLandlord() {
        return currentUser instanceof Landlord;
    }

    public static boolean isTenant() {
        return currentUser instanceof Tenant;
    }

    public static boolean isAdmin() {
        return currentUser instanceof Admin;
    }
}
