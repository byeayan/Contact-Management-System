package ContactManagementSystem;

import javax.swing.*;
import java.awt.*;

import static ContactManagementSystem.DatabaseConnector.initializeDatabase;

public class ContactManagementHomePage {
    public static void main(String[] args) {
        // Initialize database
        initializeDatabase();

        // the main frame
        JFrame frame = new JFrame("CONTACT MANAGEMENT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // 8-bit style background color
        frame.getContentPane().setBackground(new Color(24, 20, 37));

        Font pixelFont = new Font("Monospaced", Font.BOLD, 18);

        // search icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(new Color(24, 20, 37));

        JLabel titleLabel = new JLabel("CONTACT MANAGEMENT");
        titleLabel.setFont(pixelFont);
        titleLabel.setForeground(Color.WHITE);

        JButton searchIconButton = createPixelButton("🔍");
        searchIconButton.setPreferredSize(new Dimension(50, 30));

        titlePanel.add(titleLabel);
        titlePanel.add(searchIconButton);
        titlePanel.setPreferredSize(new Dimension(600, 50));

        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(5, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 80, 100));
        centerPanel.setBackground(new Color(46, 34, 59));

        JButton addContactButton = createPixelButton("ADD CONTACT");
        JButton editContactButton = createPixelButton("EDIT CONTACT");
        JButton viewContactsButton = createPixelButton("VIEW CONTACTS");
        JButton deleteContactButton = createPixelButton("DELETE CONTACT");
        JButton exitButton = createPixelButton("EXIT");

        centerPanel.add(addContactButton);
        centerPanel.add(editContactButton);
        centerPanel.add(viewContactsButton);
        centerPanel.add(deleteContactButton);
        centerPanel.add(exitButton);
        frame.add(centerPanel, BorderLayout.SOUTH);

        addContactButton.addActionListener(e -> new AddContactPage());
        viewContactsButton.addActionListener(e -> new ViewContactsPage());
        deleteContactButton.addActionListener(e -> new DeleteContactPage());
        editContactButton.addActionListener(e -> new EditContactPage());
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        searchIconButton.addActionListener(e -> openSearchWindow(frame));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JButton createPixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(214, 210, 196));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        button.setFocusPainted(false);
        return button;
    }

    private static void openSearchWindow(JFrame parentFrame) {
        JFrame searchFrame = new JFrame("Search Contact");
        searchFrame.setSize(400, 300);
        searchFrame.setLayout(new BorderLayout());
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.getContentPane().setBackground(new Color(24, 20, 37));

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(24, 20, 37));

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Monospaced", Font.BOLD, 14));
        searchField.setForeground(Color.BLACK);
        searchField.setBackground(new Color(214, 210, 196));
        searchField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JButton searchButton = createPixelButton("SEARCH");
        searchButton.addActionListener(e -> searchContact(searchField.getText(), searchFrame));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchFrame.add(searchPanel, BorderLayout.NORTH);

        searchFrame.setLocationRelativeTo(parentFrame);
        searchFrame.setVisible(true);
    }

    private static void searchContact(String query, JFrame parentFrame) {
        if (query.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter a name or number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = DatabaseConnector.searchContactInDatabase(query);

        JFrame resultFrame = new JFrame("Search Results");
        resultFrame.setSize(400, 300);
        resultFrame.setLayout(new BorderLayout());
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.getContentPane().setBackground(new Color(24, 20, 37));

        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setText(result);
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultTextArea.setForeground(Color.WHITE);
        resultTextArea.setBackground(new Color(46, 34, 59));
        resultTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        resultFrame.add(scrollPane, BorderLayout.CENTER);

        resultFrame.setLocationRelativeTo(parentFrame);
        resultFrame.setVisible(true);
    }
}
