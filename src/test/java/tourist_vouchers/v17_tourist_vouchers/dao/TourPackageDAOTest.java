package tourist_vouchers.v17_tourist_vouchers.dao;

import org.junit.jupiter.api.*;
import tourist_vouchers.v17_tourist_vouchers.model.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TourPackageDAOTest {

    private static TourPackageDAO dao;
    private static Connection conn;
    private static int createdTourId;

    @BeforeAll
    static void setup() throws SQLException {
        dao = new TourPackageDAO();
        conn = tourist_vouchers.v17_tourist_vouchers.util.DBConnection.getConnection();
    }

    @Test
    @Order(1)
    void testAddTour() throws SQLException {
        TourPackage newTour = new TourPackage(0, "Test Tour", "Test City", 1500.0, 5,
                TransportType.BUS, FoodType.BREAKFAST_ONLY, TourType.REST);
        dao.addTour(newTour);

        List<TourPackage> all = dao.getAllTours();
        createdTourId = all.get(all.size() - 1).getId();

        assertTrue(createdTourId > 0);
    }

    @Test
    @Order(2)
    void testGetTourById() throws SQLException {
        TourPackage tour = dao.getTourById(createdTourId);
        assertNotNull(tour);
        assertEquals("Test Tour", tour.getTitle());
    }

    @Test
    @Order(3)
    void testUpdateTour() throws SQLException {
        TourPackage tour = dao.getTourById(createdTourId);
        assertNotNull(tour);
        tour.setTitle("Updated Tour");
        dao.updateTour(tour);

        TourPackage updated = dao.getTourById(createdTourId);
        assertEquals("Updated Tour", updated.getTitle());
    }

    @Test
    @Order(4)
    void testGetAllTours() throws SQLException {
        List<TourPackage> tours = dao.getAllTours();
        assertNotNull(tours);
        assertTrue(tours.size() > 0);
    }

    @Test
    @Order(5)
    void testGetAllDestinations() throws SQLException {
        List<String> destinations = dao.getAllDestinations();
        assertNotNull(destinations);
        assertTrue(destinations.contains("Test City") || destinations.contains("Updated City"));
    }

    @Test
    @Order(6)
    void testDeleteTour() throws SQLException {
        dao.deleteTour(createdTourId);
        TourPackage deleted = dao.getTourById(createdTourId);
        assertNull(deleted);
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}

