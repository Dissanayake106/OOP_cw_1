import java.sql.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/news_app_db";
    private static final String DB_USER = "news_app";  // Replace with your MySQL username
    private static final String DB_PASSWORD = "abc123";  // Replace with your MySQL password

    // Connect to the database
    private Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Sign up a regular user (stored in User_Data table)
    public boolean signUp(User user) {
        String query = "INSERT INTO User_Data (First_Name, Last_Name, Age, User_Name, Email, Password, Is_Admin) VALUES (?, ?, ?, ?, ?, ?, FALSE)"; // Is_Admin is always FALSE

        if (userExists(user.getUserName(), user.getEmail())) {
            System.out.println(" ");
            return false;
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setInt(3, user.getAge());
            stmt.setString(4, user.getUserName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());  // Optionally, hash the password
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error during sign-up: " + e.getMessage());
            return false;
        }
    }

    // Sign in a user, checking for Admin or regular user by Is_Admin column
    public String signIn(String userName, String password) {
        String query = "SELECT * FROM User_Data WHERE User_Name = ? AND Password = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = rs.getBoolean("Is_Admin");
                if (isAdmin) {
                    return "Login Successful as Admin!";
                } else {
                    return "Login Successful as User!";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return "Invalid Username or Password!";
    }

    // Check if a username or email already exists in User_Data table
    private boolean userExists(String userName, String email) {
        String query = "SELECT * FROM User_Data WHERE User_Name = ? OR Email = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userName);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking existence: " + e.getMessage());
            return false;
        }
    }

}
