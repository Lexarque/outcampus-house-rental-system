package utils.seeder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class UserSeeder {
    private static final String USER_CSV_PATH = "src/file/user/users.csv";

    private static final String[][] users = {
            {"John Doe", "5551234567", "password123", "admin"},
            {"Jane Smith", "5559876543", "securePass456", "landlord"},
            {"Bob Johnson", "5554567890", "bobsPassword", "tenant"},
    };

    public static void seedUsers() {
        try {
            File file = new File(USER_CSV_PATH);
            file.getParentFile().mkdirs(); // Ensure directories exist

            boolean isNewFile = !file.exists();
            FileWriter fw = new FileWriter(file, true); // Append mode
            BufferedWriter writer = new BufferedWriter(fw);

            if (isNewFile) {
                // Write headers if file is newly created
                writer.write("userId,name,phone,password,role");
                writer.newLine();
            }

            // Write all users from the users array
            for (String[] user : users) {
                String name = user[0];
                String phone = user[1];
                String password = user[2];
                String role = user[3];

                writer.write(String.join(",", UUID.randomUUID().toString(), name, phone, password, role));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }
}
