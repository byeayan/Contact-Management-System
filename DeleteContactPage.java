package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;

public class DeleteContactPage {
    public DeleteContactPage() {
        JFrame frame = new JFrame("Delete Contact");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);

        // Layout and components
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name to Delete:");
        JTextField nameField = new JTextField();
        JButton deleteButton = new JButton("Delete");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(deleteButton);

        frame.add(panel);
        deleteButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Contact Deleted!"));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

