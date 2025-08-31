import java.sql.*;
import java.util.Scanner;

public class CarRentalSystem {

    static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    static final String USER = "root"; // your MySQL username
    static final String PASS = "Pass"; // your MySQL password
    static Connection conn;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n=== Car Rental System ===");
                System.out.println("1. Show Available Cars");
                System.out.println("2. Check Car Availability");
                System.out.println("3. Estimate Rental Amount");
                System.out.println("4. Rent a Car");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch(choice) {
                    case 1 -> showAvailableCars();
                    case 2 -> {
                        System.out.print("Enter Car ID to check availability: ");
                        int carId = sc.nextInt();
                        System.out.println(checkAvailability(carId) ? "Car is available" : "Car is not available");
                    }
                    case 3 -> {
                        System.out.print("Enter Car ID to estimate amount: ");
                        int carId = sc.nextInt();
                        System.out.print("Enter number of days: ");
                        int days = sc.nextInt();
                        double amount = estimateAmount(carId, days);
                        if(amount >= 0) {
                            System.out.println("Estimated Amount: $" + amount);
                        } else {
                            System.out.println("Car not found!");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter Car ID to rent: ");
                        int carId = sc.nextInt();
                        System.out.print("Enter number of days: ");
                        int days = sc.nextInt();
                        rentCar(carId, days);
                    }
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice!");
                }
            } while(choice != 0);

            sc.close();
            conn.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Show available cars
    public static void showAvailableCars() {
        String sql = "SELECT * FROM cars WHERE available = TRUE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAvailable Cars:");
            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Price/Day: $" + rs.getDouble("base_price_per_day"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Check availability
    public static boolean checkAvailability(int carId) {
        String sql = "SELECT available FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("available");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Estimate rental amount
    public static double estimateAmount(int carId, int days) {
        String sql = "SELECT base_price_per_day FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                double pricePerDay = rs.getDouble("base_price_per_day");
                return pricePerDay * days;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Rent a car
    public static void rentCar(int carId, int days) {
        if(!checkAvailability(carId)) {
            System.out.println("Sorry, car is not available!");
            return;
        }

        double amount = estimateAmount(carId, days);
        String sql = "UPDATE cars SET available = FALSE WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            int updated = pstmt.executeUpdate();
            if(updated > 0) {
                System.out.println("Car rented successfully! Total amount: $" + amount);
            } else {
                System.out.println("Failed to rent car.");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}

