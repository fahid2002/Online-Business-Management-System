package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ViewProductsFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel productsPanel;
    private JScrollPane scrollPane;

    public ViewProductsFrame() {
        setTitle("Twilight Thread - Products (Admin View)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 192, 203));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        backButton.addActionListener(e -> {
            new AdminPanel();
            dispose();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Twilight Thread Products", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(50, 50, 50));
        topPanel.add(title, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Products panel 
        productsPanel = new JPanel(new GridLayout(0, 4, 30, 30)); 
        productsPanel.setBackground(new Color(245, 245, 250));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ArrayList<Product> productList = Product.getAllProducts();
        for (Product p : productList) {
            productsPanel.add(createProductCard(p));
        }

        // Scroll 
        scrollPane = new JScrollPane(productsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); 
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Scroll buttons 
        JPanel scrollButtons = new JPanel();
        scrollButtons.setLayout(new BoxLayout(scrollButtons, BoxLayout.Y_AXIS));
        scrollButtons.setBackground(new Color(245, 245, 250));

        JButton scrollUp = new JButton("↑");
        JButton scrollDown = new JButton("↓");

        scrollUp.addActionListener(e -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getValue() - 100);
        });

        scrollDown.addActionListener(e -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getValue() + 100);
        });

        scrollButtons.add(scrollUp);
        scrollButtons.add(Box.createVerticalStrut(10));
        scrollButtons.add(scrollDown);

        mainPanel.add(scrollButtons, BorderLayout.EAST);

        add(mainPanel);
    }
    

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 350));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));

        //image 
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(p.getImagePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            ImageIcon placeholder = new ImageIcon("images/placeholder.jpg");
            Image img = placeholder.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }
        card.add(imgLabel, BorderLayout.CENTER);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 0, 3));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        infoPanel.add(new JLabel("Product ID: " + p.getId(), JLabel.CENTER));
        infoPanel.add(new JLabel("Name: " + p.getName(), JLabel.CENTER));
        infoPanel.add(new JLabel("Category: " + p.getCategory(), JLabel.CENTER));
        infoPanel.add(new JLabel("Price: $" + p.getPrice(), JLabel.CENTER));
        infoPanel.add(new JLabel("Stock: " + p.getStock(), JLabel.CENTER));
        infoPanel.add(new JLabel("Discount: " + p.getDiscount() + "%", JLabel.CENTER));

        card.add(infoPanel, BorderLayout.SOUTH);

        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(new LineBorder(new Color(70, 130, 180), 3, true));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
            }
        });

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewProductsFrame::new);
    }
}
