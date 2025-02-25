package ContactManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewContactsPage {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewContactsPage() {
        // Create frame with 8-bit look
        frame = new JFrame("📜 VIEW CONTACTS 📜");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(new Color(24, 20, 37)); // Dark 8-bit background

        // Header font
        Font headerFont = new Font("Monospaced", Font.BOLD, 16);

        // Table columns
        String[] columns = {"ID", "NAME", "PHONE", "EMAIL"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Monospaced", Font.BOLD, 14)); // Pixel font
        table.setBackground(new Color(214, 210, 196)); // Retro beige
        table.setForeground(Color.BLACK);
        table.setGridColor(Color.BLACK);
        table.setRowHeight(30);

        // Center cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style the table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(headerFont);
        tableHeader.setBackground(new Color(46, 34, 59)); // Dark purple
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Create scroll pane with pixel style
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Create back button
        JButton backButton = createPixelButton("⬅ BACK");
        backButton.addActionListener(e -> frame.dispose());

        // Layout setup
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(24, 20, 37));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load contacts into table
        refreshTable();
    }

    // Fetch contacts from the database and update the table
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing data
        String querySQL = "SELECT * FROM contacts";
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Helper method to create pixel-style buttons
    private JButton createPixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 14)); // Pixel font
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(214, 210, 196)); // Retro beige
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Thick border for pixel look
        button.setFocusPainted(false);
        return button;
    }
}
