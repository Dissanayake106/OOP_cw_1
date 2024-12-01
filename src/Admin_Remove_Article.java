import java.sql.*;
import java.util.Scanner;

public class Admin_Remove_Article {

    // Method to handle article removal
    public void removeArticle() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("What is the Article ID you want to remove: ");
        int articleId;

        // Validate user input for article ID
        try {
            articleId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Article ID must be a number.");
            return;
        }

        // Retrieve the article from the database
        String fetchQuery = "SELECT Title, Content FROM Article_Data WHERE Article_ID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123");
             PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery)) {

            fetchStmt.setInt(1, articleId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("Title");
                String content = rs.getString("Content");

                // Display the article details
                System.out.println("\n--- Article Details ---");
                System.out.println(centerText("Title: " + title, 96));
                System.out.println("\nContent:\n" + content);
                System.out.println("------------------------");

                // Confirm deletion
                String confirmation = "";
                while (true) {
                    System.out.print("Do you want to remove this article (yes or no): ");
                    confirmation = scanner.nextLine().trim().toLowerCase();

                    if (confirmation.equals("yes")) {
                        // Remove the article from the database
                        String deleteQuery = "DELETE FROM Article_Data WHERE Article_ID = ?";
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, articleId);
                            int rowsDeleted = deleteStmt.executeUpdate();

                            if (rowsDeleted > 0) {
                                System.out.println("Article ID: " + articleId + " has been removed successfully.");
                            } else {
                                System.out.println("Error: Could not remove the article. Please try again.");
                            }
                        }
                        break; // Exit the loop after deletion
                    } else if (confirmation.equals("no")) {
                        System.out.println("Returning to the main menu...");
                        break; // Exit the loop if user declines
                    } else {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                }
            } else {
                System.out.println("No article found with ID: " + articleId);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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
