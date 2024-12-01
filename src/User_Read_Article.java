import java.sql.*;
import java.util.Scanner;
import java.util.Date;

public class User_Read_Article {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/news_app_db";
    private static final String DB_USER = "news_app";
    private static final String DB_PASSWORD = "abc123";

    // Constants for category names
    private static final String CATEGORY_SPORTS = "Sports";
    private static final String CATEGORY_HEALTH = "Health";
    private static final String CATEGORY_TRAVEL = "Travel";
    private static final String CATEGORY_FINANCIAL = "Financial";
    private static final String CATEGORY_TECHNOLOGY = "Technology";
    private static final String CATEGORY_OTHER = "Other";

    private int userId; // User ID will be dynamically set based on login

    public User_Read_Article(int userId) {
        this.userId = userId; // Set the userId during object initialization
    }

    public void displayCategories() {
        Scanner scanner = new Scanner(System.in);
        boolean continueRED = true;

        while (continueRED) {
            displayMenu();  // Display the menu

            System.out.print("=> What is the number of the category you want to read or exit: ");
            String categoryChoice = scanner.nextLine().trim();

            // Keep asking for input until it's valid
            while (!isValidCategoryChoice(categoryChoice)) {
                System.out.println("Invalid choice, please select a valid category number or exit.");
                System.out.print("=> What is the number of the category you want to read or exit: ");
                categoryChoice = scanner.nextLine().trim();
            }

            switch (categoryChoice) {
                case "1":
                    displayArticlesByCategory(CATEGORY_SPORTS);
                    break;
                case "2":
                    displayArticlesByCategory(CATEGORY_HEALTH);
                    break;
                case "3":
                    displayArticlesByCategory(CATEGORY_TRAVEL);
                    break;
                case "4":
                    displayArticlesByCategory(CATEGORY_FINANCIAL);
                    break;
                case "5":
                    displayArticlesByCategory(CATEGORY_TECHNOLOGY);
                    break;
                case "6":
                    displayArticlesByCategory(CATEGORY_OTHER);
                    break;
                case "7":
                    continueRED = false; // Exit from RED
                    break;
            }
        }
    }

    private void displayMenu() {
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("=                               Our Categories                                  =");
        System.out.println("01. Sports");
        System.out.println("02. Health");
        System.out.println("03. Travel");
        System.out.println("04. Financial");
        System.out.println("05. Technology");
        System.out.println("06. Other");
        System.out.println("07. Exit from RED");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("===> Recommendations <===");
        System.out.println("    x Sports");
        System.out.println("    x Travel");
        System.out.println("    x Health");
        System.out.println(" ");
    }

    private boolean isValidCategoryChoice(String input) {
        return input.equals("1") || input.equals("2") || input.equals("3") ||
                input.equals("4") || input.equals("5") || input.equals("6") ||
                input.equals("7");
    }

    private void displayArticlesByCategory(String category) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Article_Id, Title FROM Article_Data WHERE Category = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, category);
                ResultSet rs = stmt.executeQuery();

                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println("=                        Articles in Category: " + category + "                       =");
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                boolean hasArticles = false;

                while (rs.next()) {
                    hasArticles = true;
                    int articleId = rs.getInt("Article_Id");
                    String title = rs.getString("Title");
                    System.out.println("Article ID: " + articleId + " | Title: " + title);
                }

                if (!hasArticles) {
                    System.out.println("No articles found in this category.");
                    return;
                }
            }

            boolean continueReading = true;
            while (continueReading) {
                System.out.print("=> Enter Article ID you want to read or 'esc' to exit: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("esc")) {
                    continueReading = false;
                } else {
                    while (!isValidArticleId(input)) {
                        System.out.print("Invalid input, please enter a valid Article ID or 'esc' to exit: ");
                        input = scanner.nextLine().trim();
                    }

                    int articleId = Integer.parseInt(input);
                    displayArticleDetails(articleId, category);  // Pass the category here
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching articles: " + e.getMessage());
        }
    }

    private boolean isValidArticleId(String input) {
        try {
            Integer.parseInt(input);  // Check if the input is a valid integer
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void displayArticleDetails(int articleId, String category) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Title, Content FROM Article_Data WHERE Article_Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, articleId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String title = rs.getString("Title");
                    String content = rs.getString("Content");

                    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                    System.out.println("=                               Article Details                                 =");
                    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                    System.out.println("Title: " + title);
                    System.out.println("Content: " + content);
                    System.out.println(" ");

                    System.out.print("=> Do you read this article (yes or no): ");
                    String readResponse = scanner.nextLine().trim().toLowerCase();

                    while (!readResponse.equals("yes") && !readResponse.equals("no")) {
                        System.out.println("Invalid response. Please enter 'yes' or 'no'.");
                        System.out.print("=> Do you read this article (yes or no): ");
                        readResponse = scanner.nextLine().trim().toLowerCase();
                    }

                    if (readResponse.equals("yes")) {
                        handleRating(articleId, title, category);  // Pass the category here
                    }
                } else {
                    System.out.println("Article not found. Please check the Article ID and try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying article details: " + e.getMessage());
        }
    }

    private void handleRating(int articleId, String title, String category) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("=> Rate this article (0 to 5): ");
        while (true) {
            try {
                int rating = Integer.parseInt(scanner.nextLine().trim());
                if (rating >= 0 && rating <= 5) {
                    System.out.println("Thank you for your feedback!");

                    // Insert the feedback into the database
                    insertFeedback(articleId, title, rating, category);
                    break;
                } else {
                    System.out.println("Invalid rating. Please rate between 0 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 5.");
            }
        }
    }

    private void insertFeedback(int articleId, String title, int rating, String category) {
        String readStatus = "yes"; // Assuming the user has read the article
        Date readDate = new Date();  // Current date and time

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO User_Feedback (User_Id, Article_Id, Read_Date, Read_Status, Rating, Category, Title) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);  // User ID (this will be dynamically assigned based on login)
                stmt.setInt(2, articleId);  // Article ID
                stmt.setTimestamp(3, new Timestamp(readDate.getTime()));  // Read Date
                stmt.setString(4, readStatus);  // Read Status
                stmt.setInt(5, rating);  // Rating
                stmt.setString(6, category);  // Category (passed dynamically)
                stmt.setString(7, title);  // Article Title

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Your feedback has been submitted successfully!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting feedback: " + e.getMessage());
        }
    }
}
