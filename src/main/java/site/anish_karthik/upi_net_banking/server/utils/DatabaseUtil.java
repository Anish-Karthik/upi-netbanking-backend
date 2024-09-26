package site.anish_karthik.upi_net_banking.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)  // Lombok makes the constructor private
public class DatabaseUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseUtil.class) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                if (connection == null || connection.isClosed()) {
                    String url = "jdbc:mysql://localhost:3306/upi-banking";
                    String username = "root";
                    String password = "rootroot";
                    connection = DriverManager.getConnection(url, username, password);
                }
            }
        }
        return connection;
    }
}
