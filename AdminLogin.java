package twilightthreadcrochet;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Twilight Thread - Admin Login");
        setSize(420, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 30, 45)); 

        // Top Logo Section
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/AdminLogin.png"));
        Image img = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setBounds(125, 10, 170, 170); 
        panel.add(logoLabel);

        // Title 
        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(240, 240, 250));
        titleLabel.setBounds(60, 190, 300, 35); 
        panel.add(titleLabel);

        JLabel subtitle = new JLabel("Enter Credentials", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(200, 200, 220));
        subtitle.setBounds(60, 225, 300, 25); 
        panel.add(subtitle);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(60, 270, 100, 25); 
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 270, 200, 28); 
        styleTextField(usernameField);
        panel.add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(60, 320, 100, 25); 
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 320, 200, 28); 
        styleTextField(passwordField);
        panel.add(passwordField);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(60, 380, 140, 35); 
        styleButton(loginButton, new Color(65, 105, 225));
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(220, 380, 140, 35); 
        styleButton(cancelButton, new Color(178, 34, 34));
        cancelButton.addActionListener(e -> System.exit(0));
        panel.add(cancelButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(60, 440, 300, 35); 
        styleButton(backButton, new Color(70, 130, 180));
        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        panel.add(backButton);

        add(panel);
    }

    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(169, 169, 169), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Admin WHERE Username=? AND Password=?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminPanel();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
