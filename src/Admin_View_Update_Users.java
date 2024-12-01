import java.sql.*;
import java.util.Scanner;

public class Admin_View_Update_Users {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/news_app_db"; // Replace with your database URL
    private static final String DB_USER = "news_app"; // Replace with your database username
    private static final String DB_PASS = "abc123"; // Replace with your database password

    private Admin_Page adminPage = new Admin_Page(); // Assuming you have an Admin_Page class for input decoration

    public void manageUsers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Scanner scanner = new Scanner(System.in)) {

            // Display User Summary at the beginning
            displayUserSummary(conn);

            // Display instructions after the user summary
            printInstructions();

            while (true) {
                System.out.println(" ");
                System.out.print("What is your purpose: ");
                String purpose = scanner.nextLine().trim().toUpperCase();

                switch (purpose) {
                    case "VUD" -> viewUserDetails(conn, scanner);
                    case "RMU" -> removeUser(conn, scanner);
                    case "PAD" -> promoteUserAsAdmin(conn, scanner);
                    case "RAD" -> removeAdminPost(conn, scanner);
                    case "ESC" -> {
                        System.out.println("Exiting to Admin Page - VUU...");
                        adminPage.adminPage("");
                        return; // Exit this method
                    }
                    default -> System.out.println("Invalid input. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Display User Summary at the start
    private void displayUserSummary(Connection conn) {
        String query = """
                SELECT 
                    COUNT(*) AS TotalUsers,
                    SUM(CASE WHEN Is_Admin = 1 THEN 1 ELSE 0 END) AS TotalAdmins,
                    SUM(CASE WHEN Is_Admin = 0 AND Age < 18 THEN 1 ELSE 0 END) AS UsersUnder18,
                    SUM(CASE WHEN Is_Admin = 0 AND Age BETWEEN 18 AND 35 THEN 1 ELSE 0 END) AS Users18to35,
                    SUM(CASE WHEN Is_Admin = 0 AND Age > 35 THEN 1 ELSE 0 END) AS UsersOver35
                FROM User_Data
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("---- Users Summary ----");
                System.out.println("Total Users: " + (rs.getInt("TotalUsers") - rs.getInt("TotalAdmins")));
                System.out.println("Total Admins: " + rs.getInt("TotalAdmins"));
                System.out.println("Users Under 18: " + rs.getInt("UsersUnder18"));
                System.out.println("Users 18 - 35: " + rs.getInt("Users18to35"));
                System.out.println("Users Over 35: " + rs.getInt("UsersOver35"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user summary: " + e.getMessage());
        }
    }

    // View User Details (VUD)
    private void viewUserDetails(Connection conn, Scanner scanner) {
        System.out.print("Enter Username of user you want details: ");
        String username = scanner.nextLine().trim();

        displayUserData(conn, username);
    }

    // Display User Data with Admin or User status
    private boolean displayUserData(Connection conn, String username) {
        String query = "SELECT * FROM User_Data WHERE User_Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("---- User Details ----");
                System.out.println("User Name: " + rs.getString("User_Name"));
                System.out.println("First Name: " + rs.getString("First_Name"));
                System.out.println("Last Name: " + rs.getString("Last_Name"));
                System.out.println("Age: " + rs.getInt("Age"));
                System.out.println("Email: " + rs.getString("Email"));
                System.out.println("Role: " + (rs.getBoolean("Is_Admin") ? "Admin" : "User"));
                return true;
            } else {
                System.out.println("User not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user data: " + e.getMessage());
            return false;
        }
    }

    // Remove Users (RMU)
    private void removeUser(Connection conn, Scanner scanner) {
        System.out.print("Enter Username of user you want to remove: ");
        String username = scanner.nextLine().trim();

        if (displayUserData(conn, username)) {
            System.out.print("Do you want to remove this user (yes or no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM User_Data WHERE User_Name = ?")) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                    System.out.println(">>> " + username + " User Removed! <<<");
                } catch (SQLException e) {
                    System.out.println("Error removing user: " + e.getMessage());
                }
            }
        }
    }

    // Promote User as Admin (PAD)
    private void promoteUserAsAdmin(Connection conn, Scanner scanner) {
        System.out.print("Enter Username of user you want to promote as Admin: ");
        String username = scanner.nextLine().trim();

        if (displayUserData(conn, username)) {
            System.out.print("Do you want to promote this user as Admin (yes or no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE User_Data SET Is_Admin = TRUE WHERE User_Name = ?")) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                    System.out.println(">>> " + username + " promoted as Admin right now!!! <<<");
                } catch (SQLException e) {
                    System.out.println("Error promoting user: " + e.getMessage());
                }
            }
        }
    }

    // Remove Admin Post (RAD)
    private void removeAdminPost(Connection conn, Scanner scanner) {
        System.out.print("Enter Username of admin you want to remove: ");
        String username = scanner.nextLine().trim();

        if (displayUserData(conn, username)) {
            System.out.print("Do you want to remove this user's Admin post (yes or no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE User_Data SET Is_Admin = FALSE WHERE User_Name = ?")) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                    System.out.println(">>> " + username + "'s Admin post Removed right now!!! <<<");
                } catch (SQLException e) {
                    System.out.println("Error removing admin post: " + e.getMessage());
                }
            }
        }
    }

    // Print Instructions
    private void printInstructions() {
        System.out.println("\n====> INSTRUCTIONS <====");
        System.out.println("VUD -> View User Details");
        System.out.println("RMU -> Remove Users");
        System.out.println("PAD -> Promote User as Admin");
        System.out.println("RAD -> Remove Admin Post");
        System.out.println("ESC -> Exit");
    }
}
