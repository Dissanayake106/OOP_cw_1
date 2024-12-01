
import java.sql.*;
import java.util.Scanner;

public class Admin_View_User_Comment {

    // Method to view all user comments from the database
    public void viewUserComments() {
        Scanner scanner = new Scanner(System.in);

        // SQL query to fetch user comments from the 'user_comment' table
        String query = "SELECT uc.User_ID, uc.Comment, uc.Date, ud.User_Name " +
                "FROM user_comment uc " +
                "JOIN User_Data ud ON uc.User_ID = ud.User_ID";

        // Connect to the database and execute the query
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // If the table is empty, inform the admin
            if (!rs.isBeforeFirst()) {
                System.out.println("No comments found in the database.");
                return;
            }

            // Display the comments in a formatted manner
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println(centerText("User Comments", 96));
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println(" ");

            // Loop through all the comments
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String comment = rs.getString("Comment");
                String date = rs.getString("Date");

                // Display the comment details
                System.out.println("User Name: " + userName);
                System.out.println("User ID: " + userId);
                System.out.println("Comment: " + comment);
                System.out.println("Date: " + date);
                System.out.println("-----------------------------------------------------------");
            }

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("Press Enter to go back to the Admin Page...");
            scanner.nextLine(); // Wait for user input to go back to the Admin Page

        } catch (SQLException e) {
            System.out.println("Error retrieving user comments: " + e.getMessage());
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
