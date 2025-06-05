package user.model;

import java.io.*;
import java.util.UUID;

public abstract class User {
    private String userId;
    private String name;
    private String phone;
    private String password;
    private String role;
    private String status = "pending";

    protected static final String CSV_PATH = "src/file/user/users.csv";

    public User() {}

    public User(String name, String phone, String password, String role) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public abstract void register(String name, String phone, String password);

    protected abstract void login(String inputPhone, String inputPassword);

    public void logout() {
        System.out.println(name + " has logged out.");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCSVPath() {
        return CSV_PATH;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
