package tourist_vouchers.v17_tourist_vouchers.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import tourist_vouchers.v17_tourist_vouchers.model.Client;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {

    private UserDAO dao;

    @BeforeEach
    void setUp() {
        dao = new UserDAO();
    }

    @Test
    void testClientExists_true() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);
            assertTrue(dao.clientExists("John"));
        }
    }

    @Test
    void testAuthenticateClientAndAdmin() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmtClient = mock(PreparedStatement.class);
        PreparedStatement stmtAdmin = mock(PreparedStatement.class);
        ResultSet rsClient = mock(ResultSet.class);
        ResultSet rsAdmin = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmtClient, stmtAdmin);
        when(stmtClient.executeQuery()).thenReturn(rsClient);
        when(rsClient.next()).thenReturn(false); // client not found
        when(stmtAdmin.executeQuery()).thenReturn(rsAdmin);
        when(rsAdmin.next()).thenReturn(true); // admin found

        when(rsAdmin.getInt("id_admin")).thenReturn(1);
        when(rsAdmin.getInt("id_usertype")).thenReturn(2);
        when(rsAdmin.getString("full_name")).thenReturn("Admin");
        when(rsAdmin.getString("phone")).thenReturn("123");
        when(rsAdmin.getString("password")).thenReturn("pass");
        when(rsAdmin.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rsAdmin.getBoolean("is_active")).thenReturn(true);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            Optional<Object> auth = dao.authenticate("Admin", "pass");
            assertTrue(auth.isPresent());
            assertTrue(auth.get() instanceof Admin);
        }
    }

    @Test
    void testRegisterClientAndAdmin() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            assertTrue(dao.registerClient("John", "123", "pass"));
            assertTrue(dao.registerAdmin("Admin", "456", "pass"));
        }
    }

    @Test
    void testActivateDeactivateClientAndAdmin() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            assertTrue(dao.activateClient(1));
            assertTrue(dao.deactivateClient(1));
            assertTrue(dao.activateAdmin(2));
            assertTrue(dao.deactivateAdmin(2));
        }
    }

    @Test
    void testValidateAdminPasswordAndPhoneExists() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            assertTrue(dao.validateAdminPassword("pass"));
            assertTrue(dao.adminPhoneExists("123"));
            assertTrue(dao.phoneExists("456"));
        }
    }

    @Test
    void testGetAllClients() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id_client")).thenReturn(1);
        when(rs.getInt("id_usertype")).thenReturn(2);
        when(rs.getString("full_name")).thenReturn("John");
        when(rs.getString("phone")).thenReturn("123");
        when(rs.getString("password")).thenReturn("pass");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rs.getBoolean("is_active")).thenReturn(true);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            List<Client> clients = dao.getAllClients();
            assertEquals(1, clients.size());
            assertEquals("John", clients.get(0).getFullName());
        }
    }
}
