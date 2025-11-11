import java.sql.*;

public class DBConnect {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mini_erp";
        String username = "root"; // your MySQL username
        String password = "Sanhad@2005#"; // your MySQL password

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connected to MySQL successfully!");

            con.close();
        } catch (Exception e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
        }
    }
}
