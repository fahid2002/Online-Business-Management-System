package twilightthreadcrochet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JFrame {

    private JLabel totalSalesLabel;

    public AdminPanel() {
        setTitle("Twilight Thread - Admin Panel");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
        loadTotalSales();
        setVisible(true);
    }

    private void initUI() {
        //frame
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 45)); 
        mainPanel.setLayout(new BorderLayout(0, 20));

        // Title & Total Sales
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 30, 45));
        topPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(230, 230, 250));

        totalSalesLabel = new JLabel("Total Sales: ৳0.00", SwingConstants.CENTER);
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalSalesLabel.setForeground(new Color(180, 180, 220));

        topPanel.add(titleLabel);
        topPanel.add(totalSalesLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 45));
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        
        buttonPanel.add(createStyledButton("Add Product", e -> new AddProductFrame(this)));
        buttonPanel.add(createStyledButton("Edit Product", e -> new EditProductFrame(this)));
        buttonPanel.add(createStyledButton("Delete Product", e -> new DeleteProductFrame(this)));
        buttonPanel.add(createStyledButton("View Products", e -> new ViewProductsFrame()));
        buttonPanel.add(createStyledButton("View Orders", e -> new ViewOrdersFrame()));
        buttonPanel.add(createStyledButton("View Users", e -> new ViewUsersFrame())); 
        buttonPanel.add(createStyledButton("View Sales History", e -> showSalesHistory()));
        buttonPanel.add(createStyledButton("Logout", e -> {
            new AdminLogin();
            dispose();
        }));

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2, true));
        button.addActionListener(action);

       
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(100, 149, 237)); }
            public void mouseExited(MouseEvent e) { button.setBackground(new Color(70, 130, 180)); }
        });

        return button;
    }

    // Load total sales
    public void loadTotalSales() {
        String query = "SELECT SUM(od.Quantity * od.Price) AS total FROM OrderDetails od";
        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                double total = rs.getDouble("total");
                totalSalesLabel.setText("Total Sales: $" + String.format("%.2f", total));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading total sales.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // sales history
    private void showSalesHistory() {
        String query = "SELECT o.OrderID, o.UserID, p.Name AS ProductName, od.Quantity, od.Price, " +
                       "(od.Quantity * od.Price) AS TotalPrice, o.OrderDate " +
                       "FROM Orders o " +
                       "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                       "JOIN Products p ON od.ProductID = p.ProductID " +
                       "ORDER BY o.OrderDate DESC";

        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            String[] columns = {"OrderID", "UserID", "Product Name", "Quantity", "Price", "Total Price", "Order Date"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("OrderID"),
                        rs.getInt("UserID"),
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price"),
                        rs.getDouble("TotalPrice"),
                        rs.getTimestamp("OrderDate")
                };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(25);
            JScrollPane scrollPane = new JScrollPane(table);

            JOptionPane.showMessageDialog(this, scrollPane, "Sales History", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sales history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
