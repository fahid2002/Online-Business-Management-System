package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class DeleteProductFrame extends JFrame {

    private JTextField idField;
    private JLabel nameLabel, categoryLabel, priceLabel, stockLabel;
    private JLabel imageLabel;
    private JButton deleteBtn;
    private AdminPanel parent;

    public DeleteProductFrame(AdminPanel parent) {
        this.parent = parent;

        setTitle("Delete Product - Twilight Thread");
        setSize(600, 800); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        // frame
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(40, 40, 55));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      
        JLabel title = new JLabel("Delete Product", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        mainPanel.add(title, BorderLayout.NORTH);

       //centre
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(50, 50, 70));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // product ID input
        JLabel idTextLabel = new JLabel("Product ID:");
        idTextLabel.setForeground(Color.WHITE);
        idTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(idTextLabel, gbc);

        idField = new JTextField();
        idField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        centerPanel.add(idField, gbc);

        JButton loadBtn = new JButton("Load Product");
        loadBtn.setBackground(new Color(70, 130, 180));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadBtn.addActionListener(e -> loadProduct());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        centerPanel.add(loadBtn, gbc);
        gbc.gridwidth = 1;

        // product info
        JLabel nameTextLabel = new JLabel("Name:");
        nameTextLabel.setForeground(Color.WHITE);
        nameTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(nameTextLabel, gbc);
        gbc.gridx = 1;
        nameLabel = createInfoLabel();
        centerPanel.add(nameLabel, gbc);

        JLabel categoryTextLabel = new JLabel("Category:");
        categoryTextLabel.setForeground(Color.WHITE);
        categoryTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(categoryTextLabel, gbc);
        gbc.gridx = 1;
        categoryLabel = createInfoLabel();
        centerPanel.add(categoryLabel, gbc);

        JLabel priceTextLabel = new JLabel("Price:");
        priceTextLabel.setForeground(Color.WHITE);
        priceTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(priceTextLabel, gbc);
        gbc.gridx = 1;
        priceLabel = createInfoLabel();
        centerPanel.add(priceLabel, gbc);

        JLabel stockTextLabel = new JLabel("Stock:");
        stockTextLabel.setForeground(Color.WHITE);
        stockTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 5;
        centerPanel.add(stockTextLabel, gbc);
        gbc.gridx = 1;
        stockLabel = createInfoLabel();
        centerPanel.add(stockLabel, gbc);

        // Image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setBorder(new LineBorder(Color.GRAY, 2, true));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        centerPanel.add(imageLabel, gbc);
        gbc.gridwidth = 1;

        // scroll
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 

       
        scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 70));
        scrollPane.getVerticalScrollBar().setForeground(new Color(70, 130, 180));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // buttons 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(new Color(40, 40, 55));

        deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 16));
        deleteBtn.setEnabled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> deleteProduct());
        btnPanel.add(deleteBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(100, 100, 100));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private void loadProduct() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) return;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Products WHERE ProductID=?")) {

            ps.setInt(1, Integer.parseInt(idText));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("Name"));
                categoryLabel.setText(rs.getString("Category"));
                priceLabel.setText("$" + rs.getDouble("Price"));
                stockLabel.setText(String.valueOf(rs.getInt("Stock")));

                try {
                    ImageIcon icon = new ImageIcon(rs.getString("ImagePath"));
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    imageLabel.setIcon(new ImageIcon("images/placeholder.jpg"));
                }

                deleteBtn.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
                clearFields();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameLabel.setText("");
        categoryLabel.setText("");
        priceLabel.setText("");
        stockLabel.setText("");
        imageLabel.setIcon(null);
        deleteBtn.setEnabled(false);
    }

    private void deleteProduct() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Products WHERE ProductID=?")) {

            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearFields();
                if (parent != null) parent.loadTotalSales();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteProductFrame(null));
    }
}
