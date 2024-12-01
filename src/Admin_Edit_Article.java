import java.sql.*;
import java.util.Scanner;

public class Admin_Edit_Article {

    // Method to edit an article
    public void editArticle() {
        Scanner scanner = new Scanner(System.in);

        // Ask for the Article ID
        System.out.print("Enter the Article ID: ");
        int articleId;
        try {
            articleId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(centerText("Invalid input. Article ID must be a number.", 96));
            return;
        }

        // this is a test comment
        // Fetch the article details from the database
        String queryFetch = "SELECT Title, Content FROM Article_Data WHERE Article_ID = ?";
        String title = null, content = null;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123");
             PreparedStatement stmtFetch = conn.prepareStatement(queryFetch)) {

            stmtFetch.setInt(1, articleId);
            ResultSet rs = stmtFetch.executeQuery();

            if (rs.next()) {
                title = rs.getString("Title");
                content = rs.getString("Content");
            } else {
                System.out.println(centerText("Error: Article with ID " + articleId + " not found.", 96));
                return;
            }

        } catch (SQLException e) {
            System.out.println(centerText("Error retrieving article: " + e.getMessage(), 96));
            return;
        }

        // Display the article's current details
        System.out.println(" ");
        System.out.println(centerText("---- Current Article ----", 96));
        System.out.println(centerText("Title: " + title, 96));
        System.out.println(content);
        System.out.println(centerText("-------------------------------", 96));
        System.out.println(" ");

        // Ask for the updated content
        System.out.println("Enter the new content for the article:");
        StringBuilder newContentBuilder = new StringBuilder();
        String line;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.isEmpty()) { // Empty line indicates the end of input
                break;
            }
            newContentBuilder.append(line).append("\n");
        }
        String newContent = newContentBuilder.toString().trim();

        // Validate input
        if (newContent.isEmpty()) {
            System.out.println(centerText("Error: Content cannot be empty. Please try again.", 96));
            return;
        }

        // Update the article in the database
        String queryUpdate = "UPDATE Article_Data SET Content = ? WHERE Article_ID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123");
             PreparedStatement stmtUpdate = conn.prepareStatement(queryUpdate)) {

            stmtUpdate.setString(1, newContent);
            stmtUpdate.setInt(2, articleId);

            int rowsUpdated = stmtUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(centerText("Article ID: " + articleId + " has been edited successfully!", 96));
            } else {
                System.out.println(centerText("Error: Could not update the article. Please try again.", 96));
            }

        } catch (SQLException e) {
            System.out.println(centerText("Error updating article: " + e.getMessage(), 96));
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

