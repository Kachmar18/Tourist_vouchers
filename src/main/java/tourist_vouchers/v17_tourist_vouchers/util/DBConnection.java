package tourist_vouchers.v17_tourist_vouchers.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static final String DATABASEURL = "jdbc:mysql://localhost:3308/tour_agency";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    private static Connection connection;
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DATABASEURL, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static boolean isDatabaseConnected() {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL, USERNAME, PASSWORD);
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
