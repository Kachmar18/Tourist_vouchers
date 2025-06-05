package tourist_vouchers.v17_tourist_vouchers.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void testGetConnectionNotNull() throws SQLException {
        Connection conn = DBConnection.getConnection();
        assertNotNull(conn, "Connection should not be null");
    }

    @Test
    void testGetConnectionIsValid() throws SQLException {
        Connection conn = DBConnection.getConnection();
        assertTrue(conn.isValid(2), "Connection should be valid");
    }

    @Test
    void testIsDatabaseConnectedReturnsTrue() {
        assertTrue(DBConnection.isDatabaseConnected(), "Database should be connected");
    }
}