package ContactManagementSystem;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ViewContactsPage {
    public ViewContactsPage() {
        JFrame frame = new JFrame("View Contacts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        // Fetch data from database
        String[] columns = {"ID", "Name", "Phone", "Email"};
        ArrayList<String[]> data = fetchContactsFromDatabase();
        String[][] tableData = data.toArray(new String[0][]);

        JTable table = new JTable(tableData, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private ArrayList<String[]> fetchContactsFromDatabase() {
        ArrayList<String[]> contacts = new ArrayList<>();
        String querySQL = "SELECT * FROM contacts";
        try (Connection conn = DatabaseConnector.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(querySQL)) {
                while (rs.next()) {
                    contacts.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching contacts: " + e.getMessage());
        }
        return contacts;
    }
}

