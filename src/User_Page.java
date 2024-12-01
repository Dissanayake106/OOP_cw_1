import java.sql.*;
import java.util.Scanner;

public class User_Page {

    private boolean profileDisplayed = false; // Flag to display profile only once

    public void userPage(String userName) {
        Scanner scanner = new Scanner(System.in);
        int userId = getUserIdFromDatabase(userName); // Retrieve user ID based on the userName

        // If the user ID is not valid, exit the page
        if (userId == -1) {
            System.out.println("User not found. Exiting...");
            return;
        }

        boolean continueUserSession = true; // Flag to control user session loop


            // Display User Interface
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("=                              Welcome to World Wire News Hub                        =");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

            // Display user profile if not already shown
            if (!profileDisplayed) {
                displayUserProfile(userName, userId); // Pass userId to display the profile
                profileDisplayed = true; // Ensure profile is displayed only once
            }

        while (continueUserSession) {

            System.out.println("Instructions:");
            System.out.println("RED -> For Read Articles");
            System.out.println("HIS -> For Read Articles History");
            System.out.println("CMM -> Give Your Valuable Comment");
            System.out.println("ESC -> For Exit Page");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");


            // Capture User Input for Actions
            System.out.print("=> Please Enter Your Choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "RED":
                    // Logic for reading articles
                    System.out.println("Redirecting to Read Articles...");
                    User_Read_Article userReadArticle = new User_Read_Article(userId); // Pass userId to User_Read_Article
                    userReadArticle.displayCategories();
                    break;
                case "HIS":
                    // Logic for viewing article history
                    System.out.println("Redirecting to Read History...");
                    User_History userHistory = new User_History(userId); // Pass userId to User_History
                    userHistory.displayHistory();
                    break;
                case "CMM":
                    // Logic for submit comment
                    System.out.println("Redirecting to Comment...");
                    User_Comment userComment = new User_Comment(userId); // Pass userId to User_Comment
                    userComment.submitComment(); // Call the method to submit comment
                    break;
                case "ESC":
                    // Exit logic
                    System.out.println("Exiting User Page...");
                    continueUserSession = false; // End the user session
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Method to get the user ID based on the userName
    private int getUserIdFromDatabase(String userName) {
        String query = "SELECT User_ID FROM User_Data WHERE User_Name = ? AND Is_Admin = FALSE";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("User_ID"); // Return the User_ID if found
            } else {
                System.out.println("User not found in the database.");
                return -1; // Return -1 if the user is not found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1; // Return -1 on error
        }
    }

    // Method to display user profile from the database
    private void displayUserProfile(String userName, int userId) {
        String query = "SELECT First_Name, Last_Name, Email FROM User_Data WHERE User_Name = ? AND Is_Admin = FALSE";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                String email = rs.getString("Email");

                System.out.println(" ");
                System.out.println(centerText("---- My Profile ----", 96));
                System.out.println(centerText("User Name: " + userName, 96));
                System.out.println(centerText("Name: " + fullName, 96));
                System.out.println(centerText("Email: " + email, 96));
                System.out.println(centerText("-------------------------------", 96));
                System.out.println(" ");
            } else {
                System.out.println("User profile not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user details: " + e.getMessage());
        }
    }

    // Center text method for decoration
    private String centerText(String text, int length) {
        int padding = (length - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
