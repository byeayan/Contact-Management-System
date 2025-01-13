package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;

import static ContactManagementSystem.DatabaseConnector.initializeDatabase;

public class ContactManagementHomePage {
    public static void main(String[] args) {
        // Initialize database
        initializeDatabase();

        // Create the main frame
        JFrame frame = new JFrame("Contact Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Contact Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton addContactButton = new JButton("Add Contact");
        JButton viewContactsButton = new JButton("View Contacts");
        JButton exitButton = new JButton("Exit");

        centerPanel.add(addContactButton);
        centerPanel.add(viewContactsButton);
        centerPanel.add(exitButton);

        frame.add(centerPanel, BorderLayout.CENTER);

        addContactButton.addActionListener(e -> new AddContactPage());
        viewContactsButton.addActionListener(e -> new ViewContactsPage());
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
