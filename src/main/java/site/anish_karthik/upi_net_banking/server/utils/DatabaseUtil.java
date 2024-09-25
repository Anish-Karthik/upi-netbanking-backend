package site.anish_karthik.upi_net_banking.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)  // Lombok makes the constructor private
public class DatabaseUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseUtil.class) {
                if (connection == null || connection.isClosed()) {
                    String url = "jdbc:mysql://localhost:3306/your_database";
                    String username = "your_username";
                    String password = "your_password";
                    connection = DriverManager.getConnection(url, username, password);
                }
            }
        }
        return connection;
    }
}
