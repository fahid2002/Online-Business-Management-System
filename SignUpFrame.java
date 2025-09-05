package twilightthreadcrochet;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpFrame extends JFrame {

    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField;

    public SignUpFrame() {
        setTitle("Twilight Thread - Sign Up");
        setSize(420, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(25, 25, 35));

        // Image
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/SignUpFrame.png"));
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBounds(90, 25, 220, 160);
        panel.add(imageLabel);

        //title 
        JLabel titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(230, 230, 250));
        titleLabel.setBounds(60, 170, 300, 40);
        panel.add(titleLabel);

        JLabel subtitle = new JLabel("Create Your Account", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(180, 180, 200));
        subtitle.setBounds(60, 200, 300, 30);
        panel.add(subtitle);

        // Username
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
        passLabel.setBounds(60, 290, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 290, 200, 28);
        styleTextField(passwordField);
        panel.add(passwordField);

        // mail
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(60, 330, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 330, 200, 28);
        styleTextField(emailField);
        panel.add(emailField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setBounds(60, 370, 100, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 370, 200, 28);
        styleTextField(phoneField);
        panel.add(phoneField);

        // Sign Up Button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(60, 430, 130, 35);
        styleButton(signupButton, new Color(60, 179, 113));
        signupButton.addActionListener(e -> signUp());
        panel.add(signupButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(220, 430, 130, 35);
        styleButton(backButton, new Color(178, 34, 34));
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

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = Conn.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT * FROM Users WHERE Username=?")) {
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Users(Username, Password, Email, PhoneNumber) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sign up successful!");
            new LoginFrame();
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error signing up!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpFrame::new);
    }
}
