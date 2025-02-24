package ContactManagementSystem;

import java.sql.*;

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
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // Search for a contact by name or phone
    public static String searchContactInDatabase(String query) {
        String searchQuery = "SELECT * FROM contacts WHERE name LIKE ? OR phone LIKE ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name") + "," +
                        resultSet.getString("phone") + "," +
                        resultSet.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Error searching database: " + e.getMessage());
        }
        return null;
    }

    // Update an existing contact
    public static boolean updateContact(String oldName, String newName, String newPhone, String newEmail) {
        String updateQuery = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newName);
            stmt.setString(2, newPhone);
            stmt.setString(3, newEmail);
            stmt.setString(4, oldName);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating contact: " + e.getMessage());
        }
        return false;
    }
}
