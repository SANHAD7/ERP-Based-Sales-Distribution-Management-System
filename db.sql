CREATE DATABASE mini_erp;
USE mini_erp;

-- USERS TABLE (Login)
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) DEFAULT 'Staff'
);

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'Admin');

-- CUSTOMERS TABLE
CREATE TABLE customers (
  customer_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(100) NOT NULL,
  phone VARCHAR(15),
  email VARCHAR(100),
  address VARCHAR(255)
);

INSERT INTO customers(customer_name, phone, email, address) VALUES
('Sahana Mart', '9876543210', 'sahana@example.com', 'Chennai'),
('Classic Traders', '9000087654', 'classic@gmail.com', 'Bangalore');

-- PRODUCTS TABLE
CREATE TABLE products (
  product_id INT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(100) NOT NULL,
  quantity INT NOT NULL,
  unit_price DOUBLE NOT NULL
);

INSERT INTO products(product_name, quantity, unit_price) VALUES
('Laptop', 50, 55000),
('Keyboard', 100, 800),
('Mouse', 150, 500);

-- ORDERS TABLE (Your existing structure restored properly)
CREATE TABLE orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(100),
  product VARCHAR(100),
  quantity INT,
  unit_price DOUBLE,
  total DOUBLE,
  payment_status VARCHAR(20)
);

INSERT INTO orders(customer_name, product, quantity, unit_price, total, payment_status) VALUES
('Sahana Mart', 'Laptop', 3, 55000, 165000, 'Paid');

-- TRIGGER: AUTOMATIC STOCK UPDATE AFTER SALE
DELIMITER $$
CREATE TRIGGER update_stock_after_order
AFTER INSERT ON orders
FOR EACH ROW
BEGIN
  UPDATE products
  SET quantity = quantity - NEW.quantity
  WHERE product_name = NEW.product;
END $$
DELIMITER ;
