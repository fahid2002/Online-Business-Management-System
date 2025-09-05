package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class user extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTable productTable;

    public user() {
        setTitle("Twilight Thread - Login");
        setSize(800, 570);
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
        add(panel);

        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(60, 50, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 28);
        styleTextField(usernameField);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(60, 100, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 28);
        styleTextField(passwordField);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(60, 150, 130, 35);
        styleButton(loginButton, new Color(70, 130, 180));
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        
        String[] columns = {"Name", "Category", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(400, 50, 360, 400);
        panel.add(scrollPane);

        loadProducts(); 
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.equals("admin") && password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
        }
    }

    private void loadProducts() {
        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Name, Category, Price FROM Products")) {

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            while (rs.next()) {
                String name = rs.getString("Name");
                String category = rs.getString("Category");
                double price = rs.getDouble("Price");
                model.addRow(new Object[]{name, category, price});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(user::new);
    }
}
