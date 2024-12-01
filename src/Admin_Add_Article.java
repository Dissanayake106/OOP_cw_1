import java.sql.*;
import java.util.Scanner;

public class Admin_Add_Article {

    // Method to handle article addition
    public void addArticle(String adminUsername) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Instructions:");
        System.out.println("Press Enter twice when done.");
        System.out.println("Use Shift+Enter for a new line (in IDEs supporting it).");
        System.out.println();

        // Collecting article details
        System.out.print("Enter Article Title: ");
        String title = scanner.nextLine().trim();

        System.out.println();
        System.out.println("Enter Article Content:");
        StringBuilder contentBuilder = new StringBuilder();
        String line;

        // Loop to allow multi-line input
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.isEmpty()) { // Empty line indicates end of input
                break;
            }
            contentBuilder.append(line).append("\n");
        }
        String content = contentBuilder.toString().trim();

        // Automatically setting the current date as publication date
        String publicationDate = java.time.LocalDate.now().toString();

        // Setting the author as the admin's username
        String author = adminUsername;

        // Automatically determining the category using the Find_Category class
        String category;
        try {
            category = Find_Category.findCategory(title, content);
        } catch (Exception e) {
            System.out.println("Error: Could not determine category. " + e.getMessage());
            return;
        }

        // Validate inputs
        if (title.isEmpty() || content.isEmpty()) {
            System.out.println("Error: Title and Content cannot be empty. Please try again.");
            return;
        }

        // SQL query to insert new article into Article_Data table
        String query = "INSERT INTO Article_Data (Title, Content, Publication_Date, Author, Category) VALUES (?, ?, ?, ?, ?)";

        // Database connection handling
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Setting parameters for the query
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, publicationDate);
            stmt.setString(4, author);
            stmt.setString(5, category);

            // Execute the query to insert the article
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Article added successfully!");
                System.out.println("Category automatically assigned: " + category);
            } else {
                System.out.println("Error: Could not add the article. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
