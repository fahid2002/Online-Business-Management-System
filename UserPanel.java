package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class UserPanel extends JFrame {

    private JPanel productsPanel;
    private JLabel cartLabel;
    private ArrayList<CartItem> cart = new ArrayList<>();
    private int userId;
    private JPanel sidebar;
    private JTextField searchField;

    public UserPanel(int userId) {
        this.userId = userId;
        setTitle("🌙 Twilight Thread - Shop");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        //  TOP BAR
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 30, 30));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        JLabel logo = new JLabel("🌙 Twilight Thread");
        logo.setFont(new Font("SansSerif", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setBorder(new EmptyBorder(5, 20, 5, 10));
        topBar.add(logo, BorderLayout.WEST);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        topButtons.setOpaque(false);

        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(70, 130, 180));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> searchProducts(searchField.getText()));

        cartLabel = new JLabel("0");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartLabel.setForeground(Color.WHITE);
        JButton cartBtn = new JButton("🛒");
        cartBtn.setBackground(new Color(70, 130, 180));
        cartBtn.setForeground(Color.WHITE);
        cartBtn.setFocusPainted(false);
        cartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartBtn.addActionListener(e -> showCart());

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(Color.RED);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.addActionListener(e -> System.exit(0));

        topButtons.add(searchField);
        topButtons.add(searchBtn);
        topButtons.add(cartBtn);
        topButtons.add(exitBtn);

        topBar.add(topButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        //  SIDEBAR 
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(255, 255, 255, 200));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(180, getHeight()));
        add(sidebar, BorderLayout.WEST);
        loadCategories();

       
        productsPanel = new JPanel() {
            
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
        productsPanel.setLayout(new GridLayout(0, 4, 20, 20));
        productsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        //SCROLL
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        loadProducts(null);
    }

    private void loadCategories() {
        sidebar.removeAll();
        JButton allBtn = new JButton("All Products");
        allBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        allBtn.addActionListener(e -> loadProducts(null));
        sidebar.add(allBtn);
        sidebar.add(Box.createVerticalStrut(10));

        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Category FROM Products")) {
            while (rs.next()) {
                String cat = rs.getString("Category");
                JButton btn = new JButton(cat);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.addActionListener(e -> loadProducts(cat));
                sidebar.add(btn);
                sidebar.add(Box.createVerticalStrut(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sidebar.revalidate();
        sidebar.repaint();
    }

    private void searchProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) loadProducts(null);
        else loadProductsByKeyword(keyword);
    }

    private void loadProducts(String category) {
        productsPanel.removeAll();
        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM Products";
            if (category != null) sql += " WHERE Category='" + category + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                JPanel card = createProductCard(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("ImagePath")
                );
                productsPanel.add(card);
            }
            productsPanel.revalidate();
            productsPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProductsByKeyword(String keyword) {
        productsPanel.removeAll();
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Products WHERE Name LIKE ? OR Category LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JPanel card = createProductCard(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("ImagePath")
                );
                productsPanel.add(card);
            }
            productsPanel.revalidate();
            productsPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createProductCard(int id, String name, String category, double price, int stock, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 300));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.NORTH);

        // Info 
        JPanel info = new JPanel(new GridLayout(4, 1));
        info.setBackground(Color.WHITE);
        info.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel idLabel = new JLabel("ID: " + id, JLabel.CENTER);
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        idLabel.setForeground(new Color(0, 0, 128)); // dark blue

        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        info.add(idLabel);
        info.add(nameLabel);
        info.add(new JLabel(category, JLabel.CENTER));
        info.add(new JLabel("$" + price, JLabel.CENTER));
        card.add(info, BorderLayout.CENTER);

        // Add button
        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> {
            addToCart(id, name, price);
            JOptionPane.showMessageDialog(this, "Added to cart!");
        });
        card.add(addBtn, BorderLayout.SOUTH);

      
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { card.setBackground(new Color(220, 240, 255)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { card.setBackground(Color.WHITE); }
        });

        return card;
    }

    private void addToCart(int productId, String name, double price) {
        for (CartItem item : cart) {
            if (item.productId == productId) {
                item.quantity++;
                updateCartLabel();
                return;
            }
        }
        cart.add(new CartItem(productId, name, price, 1));
        updateCartLabel();
    }

    private void updateCartLabel() {
        int total = cart.stream().mapToInt(i -> i.quantity).sum();
        cartLabel.setText("" + total);
    }

    private void showCart() {
        JFrame cartFrame = new JFrame("Your Cart");
        cartFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cartFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Product ID", "Product", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);

        // Bold Product ID column
        table.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            Font boldFont = new Font("SansSerif", Font.BOLD, 12);
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

        refreshCartTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton back = new JButton("Back");
        JButton checkout = new JButton("Checkout");
        back.addActionListener(e -> cartFrame.dispose());
        checkout.addActionListener(e -> checkout(model, cartFrame));
        bottom.add(back);
        bottom.add(checkout);
        panel.add(bottom, BorderLayout.SOUTH);

        cartFrame.add(panel);
        cartFrame.setVisible(true);
    }

    private void refreshCartTable(DefaultTableModel model){
        model.setRowCount(0);
        for(CartItem item: cart){
            model.addRow(new Object[]{item.productId, item.name,"$"+item.price,item.quantity});
        }
        updateCartLabel();
    }

    private void checkout(DefaultTableModel model, JFrame cartFrame){
        if(cart.isEmpty()){ JOptionPane.showMessageDialog(this,"Cart is empty!"); return; }
        try(Connection conn = Conn.getConnection()){
            conn.setAutoCommit(false);
            double total = cart.stream().mapToDouble(i->i.price*i.quantity).sum();

            PreparedStatement psOrder = conn.prepareStatement(
                    "INSERT INTO Orders(UserID,TotalAmount) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1,userId); psOrder.setDouble(2,total); psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys(); rs.next(); int orderId = rs.getInt(1);

            PreparedStatement psDetails = conn.prepareStatement(
                    "INSERT INTO OrderDetails(OrderID,ProductID,Quantity,Price) VALUES(?,?,?,?)");
            for(CartItem item: cart){
                psDetails.setInt(1,orderId); psDetails.setInt(2,item.productId);
                psDetails.setInt(3,item.quantity); psDetails.setDouble(4,item.price);
                psDetails.addBatch();
            }
            psDetails.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Checkout successful!");
            cart.clear();
            updateCartLabel();
            cartFrame.dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Checkout failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class CartItem {
        int productId;
        String name;
        double price;
        int quantity;
        CartItem(int pid, String n, double p, int q) { productId = pid; name = n; price = p; quantity = q; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserPanel(1));
    }
}
