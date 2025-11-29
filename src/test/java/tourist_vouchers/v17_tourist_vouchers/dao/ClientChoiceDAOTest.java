package tourist_vouchers.v17_tourist_vouchers.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientChoiceDAOTest {

    private ClientChoiceDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ClientChoiceDAO();
    }

    @Test
    void testUpdateClientTour_insert() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement checkStmt = mock(PreparedStatement.class);
        PreparedStatement insertStmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(checkStmt, insertStmt);
        when(checkStmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(0); // record does not exist
        when(insertStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            boolean result = dao.updateClientTour(1, 2);
            assertTrue(result);
        }
    }

    @Test
    void testUpdateClientTour_update() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement checkStmt = mock(PreparedStatement.class);
        PreparedStatement updateStmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(checkStmt, updateStmt);
        when(checkStmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1); // record exists
        when(updateStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            boolean result = dao.updateClientTour(1, 2);
            assertTrue(result);
        }
    }

    @Test
    void testClearClientTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            assertTrue(dao.clearClientTour(1));
        }
    }

    @Test
    void testGetClientsWithTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id_client")).thenReturn(1);
        when(rs.getString("full_name")).thenReturn("John Doe");
        when(rs.getString("phone")).thenReturn("123456789");
        when(rs.getInt("tour_id")).thenReturn(2);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            List<ClientChoice> clients = dao.getClientsWithTour();
            assertEquals(1, clients.size());
        }
    }

    @Test
    void testGetBookedTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("tour_id")).thenReturn(10);
        when(rs.getString("title")).thenReturn("Tour 1");
        when(rs.getString("destination")).thenReturn("Paris");
        when(rs.getDouble("price")).thenReturn(1000.0);
        when(rs.getInt("days")).thenReturn(5);
        when(rs.getString("transport")).thenReturn("CAR");
        when(rs.getString("food_type")).thenReturn("ALL_INCLUSIVE");
        when(rs.getString("tour_type")).thenReturn("ADVENTURE");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            TourPackage tour = dao.getBookedTour(1);
            assertNotNull(tour);
            assertEquals(10, tour.getId());
        }
    }
}
