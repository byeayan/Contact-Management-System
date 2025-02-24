package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteContactPage {
    private JTextField nameField;
    private JButton deleteButton;

    public DeleteContactPage() {
        JFrame frame = new JFrame("DELETE CONTACT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.getContentPane().setBackground(new Color(24, 20, 37));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(46, 34, 59));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel nameLabel = createPixelLabel("NAME TO DELETE:");
        nameField = createPixelTextField();
        deleteButton = createPixelButton("DELETE");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(nameField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(deleteButton, gbc);

        deleteButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                deleteContactFromDatabase(name);
                JOptionPane.showMessageDialog(frame, "CONTACT DELETED!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "PLEASE ENTER A NAME.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void deleteContactFromDatabase(String name) {
        String deleteSQL = "DELETE FROM contacts WHERE name = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "NO CONTACT FOUND WITH THAT NAME.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Error deleting contact: " + e.getMessage());
        }
    }

    private JLabel createPixelLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createPixelTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Monospaced", Font.BOLD, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(new Color(214, 210, 196));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return textField;
    }

    private JButton createPixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(214, 210, 196));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        button.setFocusPainted(false);
        return button;
    }
}
