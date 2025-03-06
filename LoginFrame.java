package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox showPasswordCheckBox;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(24, 20, 37));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(46, 34, 59));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel userLabel = createPixelLabel("USERNAME:");
        usernameField = createPixelTextField();

        JLabel passLabel = createPixelLabel("PASSWORD:");
        passwordField = createPixelPasswordField();

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(new Color(46, 34, 59));
        showPasswordCheckBox.setForeground(Color.WHITE);
        showPasswordCheckBox.setFont(new Font("Monospaced", Font.PLAIN, 12));
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());

        loginButton = createPixelButton("LOGIN");
        loginButton.addActionListener(e -> login());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(showPasswordCheckBox, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String role = DatabaseConnector.authenticateUser(username, password);

        if (role != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);
            dispose();
            new ContactManagementHomePage(username, role);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0); // Show password
        } else {
            passwordField.setEchoChar('●'); // Hide password
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

    private JPasswordField createPixelPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 14));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(214, 210, 196));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        passwordField.setEchoChar('*'); // Hide password by default
        return passwordField;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
