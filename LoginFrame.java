package twilightthreadcrochet;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Twilight Thread - Login");
        setSize(420, 570);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(25, 25, 35));

        // Top Image
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/LoginFrame.png"));
        Image img = icon.getImage().getScaledInstance(150, 140, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBounds(100, 20, 220, 140);
        panel.add(imageLabel);

        // title
        JLabel titleLabel = new JLabel("Twilight Thread", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(230, 230, 250));
        titleLabel.setBounds(60, 170, 300, 40);
        panel.add(titleLabel);

        JLabel subtitle = new JLabel("Login to Continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(180, 180, 200));
        subtitle.setBounds(60, 200, 300, 30);
        panel.add(subtitle);

        // username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(60, 250, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 250, 200, 28);
        styleTextField(usernameField);
        panel.add(usernameField);

        // password 
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(60, 300, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 300, 200, 28);
        styleTextField(passwordField);
        panel.add(passwordField);

        // login Button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(60, 360, 130, 35);
        styleButton(loginButton, new Color(70, 130, 180));
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(220, 360, 130, 35);
        styleButton(cancelButton, new Color(178, 34, 34));
        cancelButton.addActionListener(e -> System.exit(0));
        panel.add(cancelButton);

        // Sign Up Button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(60, 410, 290, 35);
        styleButton(signupButton, new Color(60, 179, 113));
        signupButton.addActionListener(e -> {
            new SignUpFrame();
            dispose();
        });
        panel.add(signupButton);

        // Admin Button
        JButton adminButton = new JButton("Admin Login");
        adminButton.setBounds(60, 460, 290, 35);
        styleButton(adminButton, new Color(123, 104, 238));
        adminButton.addActionListener(e -> {
            new AdminLogin();
            dispose();
        });
        panel.add(adminButton);

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

        // Check Admin table
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Admin WHERE Username=? AND Password=?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new AdminPanel();
                dispose();
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Then check Users table
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Users WHERE Username=? AND Password=?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                new UserPanel(userId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
