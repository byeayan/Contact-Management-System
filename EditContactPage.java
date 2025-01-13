package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;

public class EditContactPage {
    public EditContactPage() {
        JFrame frame = new JFrame("Edit Contact");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // Layout and components
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel searchLabel = new JLabel("Search by Name:");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JLabel nameLabel = new JLabel("New Name:");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("New Phone:");
        JTextField phoneField = new JTextField();
        JLabel emailLabel = new JLabel("New Email:");
        JTextField emailField = new JTextField();
        JButton updateButton = new JButton("Update");

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(updateButton);

        frame.add(panel);
        searchButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Contact Found (Simulated)"));
        updateButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Contact Updated!"));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

