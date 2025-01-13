package ContactManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/contact_management";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "y3llowp1ll@w"; // Replace with your MySQL password

    // Get database connection
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    // Create the contacts table if it does not exist
    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "phone VARCHAR(20) NOT NULL, " +
                "email VARCHAR(100) NOT NULL)";
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
                stmt.execute();
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}

