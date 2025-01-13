package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddContactPage {
    public AddContactPage() {
        JFrame frame = new JFrame("Add Contact");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JButton saveButton = new JButton("Save");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(saveButton);

        frame.add(panel);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                addContactToDatabase(name, phone, email);
                JOptionPane.showMessageDialog(frame, "Contact Saved!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addContactToDatabase(String name, String phone, String email) {
        String insertSQL = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, email);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error adding contact: " + e.getMessage());
        }
    }
}
