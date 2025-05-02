import auth.view.AuthenticationView;
import user.model.Admin;
import user.model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Outcampus House Rentals System!");

        AuthenticationView.printLoginMenu(scanner);
        scanner.close();
    }


}
