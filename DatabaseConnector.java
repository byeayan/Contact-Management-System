package ContactManagementSystem;

import java.sql.*;
import java.net.InetAddress;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/contact_management";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "y3llowp1ll@w"; // Update with your MySQL password

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password_hash VARCHAR(64) NOT NULL, " +
                "role ENUM('admin', 'user') DEFAULT 'user')";

        String createContactsTableSQL = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "phone VARCHAR(20) NOT NULL, " +
                "email VARCHAR(100) NOT NULL)";

        String createLoginActivityTableSQL = "CREATE TABLE IF NOT EXISTS login_activity (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "status VARCHAR(10) NOT NULL, " +
                "ip_address VARCHAR(45), " +
                "user_agent VARCHAR(100))";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSQL);
            stmt.execute(createContactsTableSQL);
            stmt.execute(createLoginActivityTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static String searchContactInDatabase(String query) {
        String searchQuery = "SELECT * FROM contacts WHERE name LIKE ? OR phone LIKE ?";
        StringBuilder result = new StringBuilder();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                result.append("Name: ").append(resultSet.getString("name"))
                        .append(" | Phone: ").append(resultSet.getString("phone"))
                        .append(" | Email: ").append(resultSet.getString("email"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Error searching database: " + e.getMessage());
        }

        return result.length() > 0 ? result.toString() : "No results found.";
    }



    // 🔹 Authenticate user & return role
    public static String authenticateUser(String username, String password) {
        String query = "SELECT password_hash, role FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String role = rs.getString("role");
                if (storedHash.equals(hashPassword(password))) {
                    recordLoginActivity(username, "SUCCESS"); // ✅ Record successful login
                    return role;
                }
            }
            recordLoginActivity(username, "FAILED"); // ❌ Record failed login attempt
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 🔹 Record login activity
    public static void recordLoginActivity(String username, String status) {
        String query = "INSERT INTO login_activity (username, status, ip_address, user_agent) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, status);
            stmt.setString(3, InetAddress.getLocalHost().getHostAddress());
            stmt.setString(4, System.getProperty("os.name"));
            stmt.executeUpdate();
            System.out.println("Login activity recorded: " + username + " - " + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 🔹 Fetch login activity logs for Admins
    public static String getLoginActivity() {
        StringBuilder logs = new StringBuilder();
        String query = "SELECT username, login_time, status, ip_address, user_agent FROM login_activity ORDER BY login_time DESC";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.append("User: ").append(rs.getString("username"))
                        .append(" | Time: ").append(rs.getTimestamp("login_time"))
                        .append(" | Status: ").append(rs.getString("status"))
                        .append(" | IP: ").append(rs.getString("ip_address"))
                        .append(" | OS: ").append(rs.getString("user_agent"))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs.toString();
    }

    // 🔹 Hash password using SHA-256
    private static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm not found!", e);
        }
    }
}
