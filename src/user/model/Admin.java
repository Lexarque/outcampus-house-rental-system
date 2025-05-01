package user.model;

import java.util.List;

public class Admin extends User {
    public Admin(User user, String adminId) {
        super(user.getUserId(), user.getName(), user.getPhone(), user.getPassword(), user.getRole());
    }

    public void viewAllUsers() {
        System.out.println("Users: ");
    }

    public void removeUser(String userId) {
        System.out.println("Successfully removed user with ID: " + userId);
    }

    public void deactivateProperty(String propertyId) {
        System.out.println("Successfully deactivated property with ID: " + propertyId);
    }

    public void changeUserStatus(String userId, String status) {
        System.out.println("Successfully changed status of user with ID: " + userId + " to: " + status);
    }
}
