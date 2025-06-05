package auth.view;

import user.model.User;

import java.util.Scanner;

public class AuthenticationView {
    public static void printLoginMenu(Scanner scanner) {
        System.out.print("Please select an option:\n1. Register\n2. Login\n3. Exit\n: ");
        int option = Integer.parseInt(scanner.nextLine());

        if (option == 3) {
            return;
        }

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        switch (option) {
            case 1:
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                System.out.print("Enter your role (landlord/tenant): ");
                String role = scanner.nextLine();
//                User user = new User(name, phone, password, role);
//                user.register();
//                user.login(phone, password);
                break;
            case 2:
//                User user2 = new User("", phone, password, "");
//                user2.login(phone, password);
//                if (user2.getStatus().isEmpty()) {
//                    do {
//                        String newPhone = scanner.nextLine();
//                        String newPassword = scanner.nextLine();
//                        user2.login(newPhone, newPassword);
//                    } while (user2.getStatus().isEmpty());
//                }
                break;
        }
    }
}
