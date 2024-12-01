import java.sql.*;
import java.util.Scanner;

public class User_Comment {

    private int userId; // User ID
    private Scanner scanner;

    public User_Comment(int userId) {
        this.userId = userId;
        this.scanner = new Scanner(System.in);
    }

    public void submitComment() {
        System.out.println("Please type your comment:");

        // Capture user's comment
        String comment = scanner.nextLine().trim();

        // Check if the comment is not empty
        if (comment.isEmpty()) {
            System.out.println("Comment cannot be empty.");
            return;
        }

        // Insert the comment into the database
        insertCommentIntoDatabase(comment);
    }

    private void insertCommentIntoDatabase(String comment) {
        String query = "INSERT INTO user_comment (User_ID, Date, Comment) VALUES (?, NOW(), ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            stmt.setInt(1, userId);  // User_ID
            stmt.setString(2, comment);  // Comment text

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Your comment has been submitted successfully!");
            } else {
                System.out.println("There was an error submitting your comment. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error submitting comment: " + e.getMessage());
        }
    }
}
