-- Create  database
CREATE DATABASE TwilightThreadDB;
USE TwilightThreadDB;

-- Admin Table
CREATE TABLE Admin (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Email VARCHAR(100),
    PhoneNumber VARCHAR(15)
);

-- Insert  admin
INSERT INTO Admin (Username, Password, Email, PhoneNumber) VALUES
('admin', 'admin123', 'admin@twilightthread.com', '01700000000');

select * from Admin;

-- Create Users Table
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Email VARCHAR(100),
    PhoneNumber VARCHAR(15)
);

select * from Users;

--  Create Products Table
CREATE TABLE Products (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Category VARCHAR(50) NOT NULL,       
    Price DECIMAL(10, 2) NOT NULL,
    Stock INT NOT NULL,
    SoldCount INT DEFAULT 0,             
    Discount DECIMAL(5, 2) DEFAULT 0.0, 
    ImagePath VARCHAR(255)               
);


select * from Products;


--  Create Orders Table
CREATE TABLE Orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TotalAmount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

select * from Orders;

--  Create OrderDetails Table 
CREATE TABLE OrderDetails (
    OrderDetailID INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

select * from OrderDetails;

CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255),
    quantity INT,
    total_amount DOUBLE,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from sales;
