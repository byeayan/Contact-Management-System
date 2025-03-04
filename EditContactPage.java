package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditContactPage {
    private JTextField searchField, nameField, phoneField, emailField;
    private JButton searchButton, updateButton;

    public EditContactPage() {
        JFrame frame = new JFrame("EDIT CONTACT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setBackground(new Color(24, 20, 37));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(46, 34, 59));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel searchLabel = createPixelLabel("SEARCH PHONE:");
        searchField = createPixelTextField();
        searchButton = createPixelButton("SEARCH");

        JLabel nameLabel = createPixelLabel("NAME:");
        nameField = createPixelTextField();
        JLabel phoneLabel = createPixelLabel("PHONE:");
        phoneField = createPixelTextField();
        JLabel emailLabel = createPixelLabel("EMAIL:");
        emailField = createPixelTextField();
        updateButton = createPixelButton("UPDATE");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(searchLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(searchField, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1; panel.add(searchButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 9; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; panel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 9; panel.add(emailField, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; panel.add(updateButton, gbc);

        searchButton.addActionListener(e -> searchContact());
        updateButton.addActionListener(e -> updateContact());

        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });

        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneField.getText().length() >= 10) {
                    e.consume(); // Ignore input if not a digit or length exceeds 10
                }
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void searchContact() {
        String searchPhone = searchField.getText().trim();
        if (searchPhone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Search field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM contacts WHERE phone = ?")) {
            stmt.setString(1, searchPhone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
                emailField.setText(rs.getString("email"));
            } else {
                JOptionPane.showMessageDialog(null, "Contact does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateContact() {
        String oldPhone = searchField.getText().trim();
        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newEmail = emailField.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(newEmail)) {
            JOptionPane.showMessageDialog(null, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updateSQL = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE phone = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setString(1, newName);
            stmt.setString(2, newPhone);
            stmt.setString(3, newEmail);
            stmt.setString(4, oldPhone);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Contact updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Contact not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
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
