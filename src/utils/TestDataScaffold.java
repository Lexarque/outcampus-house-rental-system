package utils;

import static utils.seeder.BookingSeeder.seedBookings;
import static utils.seeder.PaymentSeeder.seedPayments;
import static utils.seeder.PropertySeeder.seedProperties;
import static utils.seeder.UserSeeder.seedUsers;

public class TestDataScaffold {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Seeding users csv...");
        seedUsers();
        Thread.sleep(300);
        System.out.println("Seeding properties csv...");
        seedProperties();
        Thread.sleep(300);
        System.out.println("Seeding bookings csv...");
        seedBookings();
        Thread.sleep(300);
        System.out.println("Seeding payments csv...");
        seedPayments();
        Thread.sleep(300);
        System.out.println("Seeding complete!");
    }
}
