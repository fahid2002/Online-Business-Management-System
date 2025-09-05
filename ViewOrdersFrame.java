package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewOrdersFrame extends JFrame {

    public ViewOrdersFrame() {
        setTitle("🌙 Twilight Thread - Orders");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
          
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(230, 240, 255);
                Color color2 = new Color(245, 245, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        //  TITLE 
        JLabel title = new JLabel("Purchase History", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(0, 50, 100));
        mainPanel.add(title, BorderLayout.NORTH);

        // TABLE 
        String[] columns = {"OrderID", "UserID", "Product Name", "Quantity", "Price", "Order Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(70, 130, 180, 100));
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);

        // CENTER ALIGN QUANTITY & PRICE 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // BOLD ORDERID & PRODUCT NAME
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            Font boldFont = new Font("SansSerif", Font.BOLD, 14);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(boldFont);
                c.setForeground(new Color(0, 0, 128));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            Font boldFont = new Font("SansSerif", Font.BOLD, 14);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(boldFont);
                c.setForeground(new Color(0, 50, 100));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BACK BUTTON 
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(backButton);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        
        populateTable(model);
    }

    private void populateTable(DefaultTableModel model) {
        String query = "SELECT o.OrderID, o.UserID, p.Name AS ProductName, od.Quantity, od.Price, o.OrderDate " +
                       "FROM Orders o " +
                       "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                       "JOIN Products p ON od.ProductID = p.ProductID " +
                       "ORDER BY o.OrderDate DESC";

        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("OrderID"),
                        rs.getInt("UserID"),
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        "$" + rs.getDouble("Price"),
                        rs.getTimestamp("OrderDate")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching orders.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewOrdersFrame::new);
    }
}
