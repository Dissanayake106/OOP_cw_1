import java.sql.*;
import java.util.Scanner;

public class Admin_Page {

    // Method to display the Admin Page Dashboard
    public void adminPage(String userName) {
        adminPage(userName, true); // Default behavior is to display the profile
    }

    // Overloaded method with a flag to control profile display
    public void adminPage(String userName, boolean showProfile) {
        Scanner scanner = new Scanner(System.in);
        boolean isFirstDisplay = true; // Ensure full display only on the first iteration

        while (true) { // Keep the admin page running until the user exits
            if (isFirstDisplay) {
                System.out.println(" ");
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println(centerText("Admin-Page 'World Wire' News Articles", 96));
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println(" ");

                // Display Admin details only if the flag is true
                if (showProfile) {
                    getAdminDetails(userName);
                    showProfile = false; // Show the profile only once
                }

                // Display Settings options with decorative styling
                System.out.println("\t\t\t\t\t=== Settings ===");
                System.out.println("\t\tType 'VUU' -> View User Summary & Update Users");
                System.out.println("\t\tType 'ADA' -> Add Articles");
                System.out.println("\t\tType 'VSA' -> View Summary Of Articles");
                System.out.println("\t\tType 'EDA' -> Edit Articles");
                System.out.println("\t\tType 'RMA' -> Remove Articles");
                System.out.println("\t\tType 'VUC' -> View User's Comments");
                System.out.println("\t\tType 'ESC' -> Exit to News Wire");
                System.out.println(" ");
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println(" ");

                isFirstDisplay = false; // Ensure this block doesn't run again on invalid input
            }

            // Get input from admin
            System.out.print("What Do You Want : ");
            String choice = scanner.nextLine().toUpperCase();

            // Routing to different functionalities based on user input
            switch (choice) {
                case "VUU":
                    System.out.println(centerText("Going to View User Summary & Update Users...", 96));
                    Admin_View_Update_Users adminViewUpdateUsers = new Admin_View_Update_Users(); // Create instance of Admin_View_Update_Users
                    adminViewUpdateUsers.manageUsers(); // Call the correct method to view and update users
                    isFirstDisplay = true; // Redisplay the full page after exiting VUU
                    break;

                case "ADA":
                    System.out.println(centerText("Going to Add Articles...", 96));
                    // Create instance of Admin_Add_Article and call the addArticle method
                    Admin_Add_Article adminAddArticle = new Admin_Add_Article();
                    adminAddArticle.addArticle(userName);  // Pass the admin's username to add an article
                    isFirstDisplay = true;  // Redisplay the admin dashboard after adding the article
                    break;

                case "EDA":
                    System.out.println(centerText("Going to Edit Articles...", 96));
                    // Create instance of Admin_Edit_Article and call the editArticle method
                    Admin_Edit_Article adminEditArticle = new Admin_Edit_Article();
                    adminEditArticle.editArticle(); // Edit an article by ID
                    isFirstDisplay = true; // Redisplay the admin dashboard after editing an article
                    break;

                case "VSA":
                    System.out.println(centerText("Going to View Summary of Articles...", 96));
                    Admin_View_Article_Summary adminViewSummary = new Admin_View_Article_Summary();
                    adminViewSummary.viewSummary(userName);  // Pass the admin's username to view article summary
                    isFirstDisplay = true; // Redisplay after viewing the summary
                    break;

                case "RMA":
                    System.out.println(centerText("Going to Remove Articles...", 96));
                    Admin_Remove_Article adminRemoveArticle = new Admin_Remove_Article();
                    adminRemoveArticle.removeArticle(); // Call the removeArticle method
                    isFirstDisplay = true; // Redisplay the admin dashboard after removing an article
                    break;

                case "VUC":
                    System.out.println(centerText("Going to View User's Comments...", 96));
                    Admin_View_User_Comment adminViewUserComment = new Admin_View_User_Comment();
                    adminViewUserComment.viewUserComments(); // Call the viewUserComments method
                    isFirstDisplay = true; // Redisplay after viewing user comments
                    break;

                case "ESC":
                    System.out.println(centerText("Exit Admin Page!", 96)); // Display exit message
                    return; // Exit the method

                default:
                    System.out.println(centerText("Invalid choice. Please try again.", 96));
                    break;
            }
        }
    }

    // Method to get Admin details from the database
    public void getAdminDetails(String userName) {
        String query = "SELECT First_Name, Last_Name, Email FROM User_Data WHERE User_Name = ? AND Is_Admin = TRUE";

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
            }
        } catch (SQLException e) {
            System.out.println(centerText("Error retrieving admin details: " + e.getMessage(), 96));
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
