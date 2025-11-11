import java.sql.*;
import java.util.*;

class Customer {
    int customerId;
    String name;
    String region;

    Customer(int customerId, String name, String region) {
        this.customerId = customerId;
        this.name = name;
        this.region = region;
    }
}

class Order {
    int orderId;
    Customer customer;
    String product;
    int quantity;
    double unitPrice;
    boolean paymentDone;

    Order(int orderId, Customer customer, String product, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.paymentDone = false;
    }

    double getTotal() {
        return quantity * unitPrice;
    }

    void markPaymentDone() {
        paymentDone = true;
    }

    void displayInvoice() {
        System.out.println("\n----- Invoice -----");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.name + " (" + customer.region + ")");
        System.out.println("Product: " + product);
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: ₹" + unitPrice);
        System.out.println("Total Amount: ₹" + getTotal());
        System.out.println("Payment Status: " + (paymentDone ? "Paid ✅" : "Pending ❌"));
    }
}

public class MiniERP {

    // MySQL connection details
    static String url = "jdbc:mysql://localhost:3306/mini_erp";
    static String username = "root";
    static String password = "Sanhad@2005#";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Customer> customers = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        System.out.println("=== SAP ERP Simulation - Sales & Distribution ===");

        // Add some customers
        customers.add(new Customer(101, "Ayesha Traders", "Chennai"));
        customers.add(new Customer(102, "Farooq Electronics", "Hyderabad"));
        customers.add(new Customer(103, "Sahana Mart", "Bangalore"));

        while (true) {
            System.out.print("\nEnter Order ID: ");
            int id = sc.nextInt();

            System.out.println("Select Customer (ID):");
            for (Customer c : customers) {
                System.out.println(c.customerId + " - " + c.name + " (" + c.region + ")");
            }

            int custId = sc.nextInt();
            Customer selected = customers.stream().filter(c -> c.customerId == custId).findFirst().orElse(null);

            sc.nextLine(); // consume newline
            System.out.print("Product: ");
            String product = sc.nextLine();
            System.out.print("Quantity: ");
            int qty = sc.nextInt();
            System.out.print("Unit Price: ");
            double price = sc.nextDouble();

            Order o = new Order(id, selected, product, qty, price);
            orders.add(o);
            o.displayInvoice();

            // Save order to DB
            saveOrderToDB(o);

            System.out.print("\nMark payment as done? (yes/no): ");
            String pay = sc.next();
            if (pay.equalsIgnoreCase("yes")) {
                o.markPaymentDone();
                updatePaymentStatus(o);
            }

            System.out.print("\nDo you want to add another order? (yes/no): ");
            String ch = sc.next();
            if (ch.equalsIgnoreCase("no")) break;
        }

        // Display Sales Summary
        System.out.println("\n=== Sales Summary ===");
        double totalSales = 0;
        long paidOrders = 0;
        for (Order o : orders) {
            totalSales += o.getTotal();
            if (o.paymentDone) paidOrders++;
        }

        System.out.println("Total Orders: " + orders.size());
        System.out.println("Orders Paid: " + paidOrders);
        System.out.println("Total Sales Value: ₹" + totalSales);
        System.out.println("\nThank you for using Mini ERP System!");
    }

    // Method to save order to MySQL
    static void saveOrderToDB(Order o) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO orders(order_id, customer_name, product, quantity, unit_price, total, payment_status) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, o.orderId);
            ps.setString(2, o.customer.name);
            ps.setString(3, o.product);
            ps.setInt(4, o.quantity);
            ps.setDouble(5, o.unitPrice);
            ps.setDouble(6, o.getTotal());
            ps.setString(7, o.paymentDone ? "Paid" : "Pending");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Failed to save order to DB!");
            e.printStackTrace();
        }
    }

    // Method to update payment status in DB
    static void updatePaymentStatus(Order o) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE orders SET payment_status=? WHERE order_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "Paid");
            ps.setInt(2, o.orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Failed to update payment status!");
            e.printStackTrace();
        }
    }
}
