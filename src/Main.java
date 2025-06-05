import auth.view.AuthenticationView;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static final String SESSION_FILE_CSV_DIR = "src/file/session/session.csv";

    public static void main(String[] args) {
        // Initialize the session file directory
        File sessionFile = new File(SESSION_FILE_CSV_DIR);
        sessionFile.getParentFile().mkdirs();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Outcampus House Rentals System!");

        AuthenticationView.printLoginMenu(scanner);
        scanner.close();
    }


}
