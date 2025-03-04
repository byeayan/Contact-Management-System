package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

public class AddContactPage {
    private JTextField nameField, phoneField, emailField;
    private JButton saveButton;

    public AddContactPage() {
        JFrame frame = new JFrame("ADD CONTACT");
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

        JLabel nameLabel = createPixelLabel("NAME:");
        nameField = createPixelTextField();
        JLabel phoneLabel = createPixelLabel("PHONE:");
        phoneField = createPixelTextField();
        JLabel emailLabel = createPixelLabel("EMAIL:");
        emailField = createPixelTextField();
        saveButton = createPixelButton("SAVE");

        // Add constraints for phone number (digits only, max length 10)
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneField.getText().length() >= 10) {
                    e.consume(); // Ignore input if not a digit or length exceeds 10
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(emailField, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "FILL ALL FIELDS!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (phone.length() != 10) {
                JOptionPane.showMessageDialog(frame, "PHONE NUMBER MUST BE 10 DIGITS!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "INVALID EMAIL FORMAT!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addContactToDatabase(name, phone, email);
            JOptionPane.showMessageDialog(frame, "CONTACT SAVED!");
            frame.dispose();
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addContactToDatabase(String name, String phone, String email) {
        String insertSQL = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
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
