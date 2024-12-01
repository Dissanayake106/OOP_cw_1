import java.sql.*;

public class Admin_View_Article_Summary {

    // Method to view the summary of articles
    public void viewSummary(String userName) {
        // Print header with decorative borders
        System.out.println(centerText("---- View Summary of Articles ----", 96));
        System.out.println(centerText("------------------------------------------------------------", 96));

        // SQL query to fetch article details from Article_Data table
        String query = "SELECT Article_Id, Publication_Date, Author, Category, Title FROM Article_Data";

        // Connecting to the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/news_app_db", "news_app", "abc123"); // Replace with your DB connection details
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Print the table header with clear separation of columns
            System.out.println(centerText("Article ID | Publication Date | Author           | Category                | Title                                                        ", 96));
            System.out.println(centerText("------------------------------------------------------------", 96));

            // Loop through the result set and display each article
            while (rs.next()) {
                int articleId = rs.getInt("Article_Id");
                Date pubDate = rs.getDate("Publication_Date");
                String author = rs.getString("Author");
                String category = rs.getString("Category");
                String title = rs.getString("Title");

                // Format the data to ensure it aligns properly in columns
                String formattedArticleId = String.format("%-10d", articleId);  // Pad Article ID to 10 characters
                String formattedPubDate = pubDate.toString();                    // Date formatted as yyyy-mm-dd
                String formattedAuthor = padRight(author, 20);                    // Pad author to 20 characters
                String formattedCategory = padRight(category, 20);                // Pad category to 20 characters
                String formattedTitle = padRight(title, 50);                      // Pad title to 50 characters

                // Display each article in one row with clear column separation
                System.out.println(centerText(formattedArticleId + " | " + formattedPubDate + " | " + formattedAuthor + " | " + formattedCategory + " | " + formattedTitle, 96));
            }

        } catch (SQLException e) {
            System.out.println(centerText("Error retrieving article summary: " + e.getMessage(), 96));
        }
    }

    // Method to center the text for display
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

    // Method to pad strings to the right for column alignment
    private String padRight(String str, int length) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(" ");  // Pad with spaces to the right
        }
        return sb.toString();
    }
}
