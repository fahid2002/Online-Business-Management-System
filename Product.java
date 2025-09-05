package twilightthreadcrochet;

import java.sql.*;
import java.util.ArrayList;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private int soldCount;
    private double discount;
    private String imagePath;

    public Product(int id, String name, String category, double price, int stock, int soldCount, double discount, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.soldCount = soldCount;
        this.discount = discount;
        this.imagePath = imagePath;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getSoldCount() { return soldCount; }
    public double getDiscount() { return discount; }
    public String getImagePath() { return imagePath; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setSoldCount(int soldCount) { this.soldCount = soldCount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

  

    // Fetch all products
    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = Conn.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
            while(rs.next()){
                Product p = new Product(
                    rs.getInt("ProductID"),
                    rs.getString("Name"),
                    rs.getString("Category"),
                    rs.getDouble("Price"),
                    rs.getInt("Stock"),
                    rs.getInt("SoldCount"),
                    rs.getDouble("Discount"),
                    rs.getString("ImagePath")
                );
                products.add(p);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    // Add product
    public boolean addProduct() {
        try (Connection conn = Conn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Products (Name, Category, Price, Stock, SoldCount, Discount, ImagePath) VALUES (?,?,?,?,?,?,?)"
            );
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, soldCount);
            ps.setDouble(6, discount);
            ps.setString(7, imagePath);
            ps.executeUpdate();
            return true;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Update product
    public boolean updateProduct() {
        try (Connection conn = Conn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE Products SET Name=?, Category=?, Price=?, Stock=?, SoldCount=?, Discount=?, ImagePath=? WHERE ProductID=?"
            );
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, soldCount);
            ps.setDouble(6, discount);
            ps.setString(7, imagePath);
            ps.setInt(8, id);
            ps.executeUpdate();
            return true;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Delete product
    public boolean deleteProduct() {
        try (Connection conn = Conn.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Products WHERE ProductID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    
    public static void main(String[] args) {
        // Example: fetch all products
        ArrayList<Product> products = Product.getAllProducts();
        for(Product p : products){
            System.out.println(p.getId() + " - " + p.getName() + " - $" + p.getPrice());
        }
    }
}
