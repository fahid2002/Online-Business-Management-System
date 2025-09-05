package twilightthreadcrochet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.sql.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class AddProductFrame extends JFrame {

    private JTextField nameField, categoryField, priceField, stockField, soldField, discountField, imagePathField;
    private int editId = -1; 
    private AdminPanel parent;

    // Constructor
    public AddProductFrame(AdminPanel parent) {
        this.parent = parent;
        initUI("Add Product");
    }

   
    public AddProductFrame(AdminPanel parent, int id, String name, String category, double price, int stock,
                           int soldCount, double discount, String imagePath) {
        this.parent = parent;
        this.editId = id;
        initUI("Edit Product");

        nameField.setText(name);
        categoryField.setText(category);
        priceField.setText(String.valueOf(price));
        stockField.setText(String.valueOf(stock));
        soldField.setText(String.valueOf(soldCount));
        discountField.setText(String.valueOf(discount));
        imagePathField.setText(imagePath);
    }

    private void initUI(String titleText) {
        setTitle(titleText + " - Twilight Thread");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // background
        getContentPane().setBackground(new Color(10,25,70));
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10,25,70)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel(titleText, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        // Labels and TextFields
        String[] labelsText = {"Name:", "Category:", "Price:", "Stock:", "Sold Count:", "Discount (%):", "Image Path:"};
        JTextField[] fields = new JTextField[7];

        for(int i=0; i<labelsText.length; i++){
            JLabel lbl = new JLabel(labelsText[i]);
            lbl.setForeground(Color.WHITE);
            gbc.gridx = 0; gbc.gridy = i+1;
            panel.add(lbl, gbc);

            fields[i] = new JTextField(20);
            fields[i].setBackground(new Color(30,40,90)); 
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(Color.WHITE); 
            gbc.gridx = 1; panel.add(fields[i], gbc);
        }

        nameField = fields[0];
        categoryField = fields[1];
        priceField = fields[2];
        stockField = fields[3];
        soldField = fields[4];
        discountField = fields[5];
        imagePathField = fields[6];

        // Browse button
        JButton browseBtn = new JButton("Browse");
        browseBtn.setBackground(new Color(70, 130, 180));
        browseBtn.setForeground(Color.WHITE);
        browseBtn.addActionListener(e -> browseImage());
        gbc.gridx = 2; gbc.gridy = 7;
        panel.add(browseBtn, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(10,25,70));
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        JButton saveBtn = new JButton(editId == -1 ? "Add Product" : "Save Changes");
        saveBtn.setBackground(new Color(70, 130, 180));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 16));
        saveBtn.addActionListener(e -> saveProduct());
        buttonPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);

        add(panel);
        setVisible(true);
    }

    private void browseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg","png","jpeg","gif"));
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            imagePathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveProduct() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String imagePath = imagePathField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String soldText = soldField.getText().trim();
        String discountText = discountField.getText().trim();

        if(name.isEmpty() || category.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all required fields.","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = priceText.isEmpty()?0:Double.parseDouble(priceText);
            int stock = stockText.isEmpty()?0:Integer.parseInt(stockText);
            int soldCount = soldText.isEmpty()?0:Integer.parseInt(soldText);
            double discount = discountText.isEmpty()?0:Double.parseDouble(discountText);

            if(price<0 || stock<0 || soldCount<0 || discount<0){
                JOptionPane.showMessageDialog(this,"Numeric values cannot be negative.","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            try(Connection conn = Conn.getConnection()){
                PreparedStatement ps;
                if(editId==-1){ // Add
                    ps = conn.prepareStatement(
                        "INSERT INTO Products (Name, Category, Price, Stock, SoldCount, Discount, ImagePath) VALUES (?,?,?,?,?,?,?)"
                    );
                    ps.setString(1,name);
                    ps.setString(2,category);
                    ps.setDouble(3,price);
                    ps.setInt(4,stock);
                    ps.setInt(5,soldCount);
                    ps.setDouble(6,discount);
                    ps.setString(7,imagePath);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this,"Product added successfully!");
                } else { // Edit
                    ps = conn.prepareStatement(
                        "UPDATE Products SET Name=?, Category=?, Price=?, Stock=?, SoldCount=?, Discount=?, ImagePath=? WHERE ProductID=?"
                    );
                    ps.setString(1,name);
                    ps.setString(2,category);
                    ps.setDouble(3,price);
                    ps.setInt(4,stock);
                    ps.setInt(5,soldCount);
                    ps.setDouble(6,discount);
                    ps.setString(7,imagePath);
                    ps.setInt(8,editId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this,"Product updated successfully!");
                }

              
            } catch(SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Database error.","Error",JOptionPane.ERROR_MESSAGE);
            }

        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Price, Stock, Sold Count, and Discount must be valid numbers.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new AddProductFrame(null));
    }
}
