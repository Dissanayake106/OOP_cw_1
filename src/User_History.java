import java.sql.*;

public class User_History {

    private int userId; // User ID for fetching history

    // Constructor to initialize the user ID
    public User_History(int userId) {
        this.userId = userId;
    }

    // Method to display the user's article reading history
    public void displayHistory() {
        String query = "SELECT Article_ID, Title, Read_Date FROM user_feedback WHERE User_ID = ? ORDER BY Read_Date DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId); // Set the user ID parameter
            ResultSet rs = stmt.executeQuery();

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("=                            Your Reading History                              =");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.printf("%-15s%-25s%-40s%n", "Article ID", "Read Date", "Article Title");
            System.out.println("--------------------------------------------------------------------------------");

            boolean hasHistory = false; // Flag to check if history exists

            while (rs.next()) {
                hasHistory = true;
                int articleId = rs.getInt("Article_ID");
                String title = rs.getString("Title");
                Timestamp readDate = rs.getTimestamp("Read_Date");

                // Display the article details in a formatted row
                System.out.printf("%-15d%-25s%-40s%n", articleId, readDate, title);
            }

            if (!hasHistory) {
                System.out.println("You have not read any articles yet.");
            }

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        } catch (SQLException e) {
            System.out.println("Error retrieving reading history: " + e.getMessage());
        }
    }
}
