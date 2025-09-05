package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EditProductFrame extends JFrame {

    private JTextField idField, nameField, categoryField, priceField, stockField, imagePathField;
    private JLabel imagePreview;
    private AdminPanel parent;

    public EditProductFrame(AdminPanel parent) {
        this.parent = parent;

        setTitle("Edit Product - Twilight Thread");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(40, 40, 55));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Edit Product", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        mainPanel.add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(50, 50, 70));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Product ID
        formPanel.add(createLabel("Product ID to edit:"), createGbc(0, row));
        idField = createTextField();
        formPanel.add(idField, createGbc(1, row++));
        JButton loadBtn = new JButton("Load Product");
        styleButton(loadBtn, new Color(70, 130, 180));
        loadBtn.addActionListener(e -> loadProduct());
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(loadBtn, createGbc(0, row++));
        gbc.gridwidth = 1;

        // name
        formPanel.add(createLabel("Name:"), createGbc(0, row));
        nameField = createTextField();
        formPanel.add(nameField, createGbc(1, row++));

        // category
        formPanel.add(createLabel("Category:"), createGbc(0, row));
        categoryField = createTextField();
        formPanel.add(categoryField, createGbc(1, row++));

        // price
        formPanel.add(createLabel("Price:"), createGbc(0, row));
        priceField = createTextField();
        formPanel.add(priceField, createGbc(1, row++));

        // stock
        formPanel.add(createLabel("Stock:"), createGbc(0, row));
        stockField = createTextField();
        formPanel.add(stockField, createGbc(1, row++));

        // image Path
        formPanel.add(createLabel("Image Path:"), createGbc(0, row));
        imagePathField = createTextField();
        formPanel.add(imagePathField, createGbc(1, row++));

        // Browse button
        JButton browseBtn = new JButton("Browse");
        styleButton(browseBtn, new Color(70, 130, 180));
        browseBtn.setPreferredSize(new Dimension(100, 30));
        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Select Product Image");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                imagePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                ImageIcon icon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreview.setIcon(new ImageIcon(img));
            }
        });
        JPanel browsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        browsePanel.setBackground(new Color(50, 50, 70));
        browsePanel.add(browseBtn);
        gbc.gridx = 1; gbc.gridy = row++;
        formPanel.add(browsePanel, gbc);

        // Image 
        imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(100, 100));
        imagePreview.setBorder(new LineBorder(Color.GRAY, 2));
        gbc.gridx = 1; gbc.gridy = row++;
        formPanel.add(imagePreview, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(new Color(40, 40, 55));

        JButton saveBtn = new JButton("Save Changes");
        styleButton(saveBtn, new Color(34, 139, 34));
        saveBtn.addActionListener(e -> saveChanges());
        btnPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, Color.RED);
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        return field;
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadProduct() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) return;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Products WHERE ProductID=?")) {
            ps.setInt(1, Integer.parseInt(idText));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("Name"));
                categoryField.setText(rs.getString("Category"));
                priceField.setText(String.valueOf(rs.getDouble("Price")));
                stockField.setText(String.valueOf(rs.getInt("Stock")));
                imagePathField.setText(rs.getString("ImagePath"));

                try {
                    ImageIcon icon = new ImageIcon(rs.getString("ImagePath"));
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    imagePreview.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveChanges() {
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Products SET Name=?, Category=?, Price=?, Stock=?, ImagePath=? WHERE ProductID=?")) {

            ps.setString(1, nameField.getText().trim());
            ps.setString(2, categoryField.getText().trim());
            ps.setDouble(3, Double.parseDouble(priceField.getText().trim()));
            ps.setInt(4, Integer.parseInt(stockField.getText().trim()));
            ps.setString(5, imagePathField.getText().trim());
            ps.setInt(6, Integer.parseInt(idField.getText().trim()));

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                if (parent != null) parent.loadTotalSales();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditProductFrame(null));
    }
}
