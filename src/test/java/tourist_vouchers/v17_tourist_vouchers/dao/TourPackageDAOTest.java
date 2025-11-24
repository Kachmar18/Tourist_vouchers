package tourist_vouchers.v17_tourist_vouchers.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourPackageDAOTest {

    private TourPackageDAO dao;

    @BeforeEach
    void setUp() {
        dao = new TourPackageDAO();
    }

    @Test
    void testGetAllTours() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("tour_id")).thenReturn(1);
        when(rs.getString("title")).thenReturn("Tour 1");
        when(rs.getString("destination")).thenReturn("Paris");
        when(rs.getDouble("price")).thenReturn(1000.0);
        when(rs.getInt("days")).thenReturn(5);
        when(rs.getString("transport")).thenReturn("CAR");
        when(rs.getString("food_type")).thenReturn("ALL_INCLUSIVE");
        when(rs.getString("tour_type")).thenReturn("ADVENTURE");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            List<TourPackage> tours = dao.getAllTours();
            assertEquals(1, tours.size());
            assertEquals("Tour 1", tours.get(0).getTitle());
        }
    }

    @Test
    void testAddTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        TourPackage tour = new TourPackage(1, "T", "D", 100, 1, TransportType.CAR, FoodType.ALL_INCLUSIVE, TourType.CRUISE);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);
            dao.addTour(tour);
        }
    }

    @Test
    void testUpdateTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        TourPackage tour = new TourPackage(1, "T", "D", 100, 1, TransportType.CAR, FoodType.ALL_INCLUSIVE, TourType.REST);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);
            dao.updateTour(tour);
        }
    }

    @Test
    void testDeleteTour() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);
            dao.deleteTour(1);
        }
    }

    @Test
    void testGetAllDestinations() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("destination")).thenReturn("Paris");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);
            List<String> destinations = dao.getAllDestinations();
            assertEquals(1, destinations.size());
            assertEquals("Paris", destinations.get(0));
        }
    }

    @Test
    void testGetTourById() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("tour_id")).thenReturn(1);
        when(rs.getString("title")).thenReturn("Tour 1");
        when(rs.getString("destination")).thenReturn("Paris");
        when(rs.getDouble("price")).thenReturn(1000.0);
        when(rs.getInt("days")).thenReturn(5);
        when(rs.getString("transport")).thenReturn("CAR");
        when(rs.getString("food_type")).thenReturn("ALL_INCLUSIVE");
        when(rs.getString("tour_type")).thenReturn("ADVENTURE");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(conn);

            TourPackage tour = dao.getTourById(1);
            assertNotNull(tour);
            assertEquals(1, tour.getId());
            assertEquals("Tour 1", tour.getTitle());
        }
    }
}
