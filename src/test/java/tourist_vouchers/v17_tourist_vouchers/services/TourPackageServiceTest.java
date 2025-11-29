package tourist_vouchers.v17_tourist_vouchers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.dao.TourPackageDAO;
import tourist_vouchers.v17_tourist_vouchers.model.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourPackageServiceTest {

    private TourPackageDAO daoMock;
    private TourPackageService service;

    @BeforeEach
    void setUp() {
        daoMock = mock(TourPackageDAO.class);
        service = new TourPackageService(daoMock);
    }

    @Test
    void testGetAllTours() throws SQLException {
        List<TourPackage> mockList = List.of(new TourPackage());
        when(daoMock.getAllTours()).thenReturn(mockList);

        List<TourPackage> result = service.getAllTours();
        assertEquals(mockList, result);
        verify(daoMock).getAllTours();
    }

    @Test
    void testGetTourById() {
        TourPackage mockTour = new TourPackage();
        when(daoMock.getTourById(1)).thenReturn(mockTour);

        TourPackage result = service.getTourById(1);
        assertEquals(mockTour, result);
        verify(daoMock).getTourById(1);
    }

    @Test
    void testFilterTours() {
        TourPackage t1 = new TourPackage(1, "Title", "Paris", 1000, 5,
                TransportType.BUS, FoodType.ALL_INCLUSIVE, TourType.REST);
        TourPackage t2 = new TourPackage(2, "Title2", "Rome", 500, 3,
                TransportType.PLANE, FoodType.BREAKFAST_ONLY, TourType.EXCURSION);

        try {
            when(daoMock.getAllTours()).thenReturn(List.of(t1, t2));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<TourPackage> filtered = service.filterTours("Paris", null, null, null, null, null);
        assertEquals(1, filtered.size());
        assertEquals(t1, filtered.get(0));

        filtered = service.filterTours(null, 600.0, null, null, null, null);
        assertEquals(1, filtered.size());
        assertEquals(t2, filtered.get(0));
    }

    @Test
    void testGetAllDestinations() throws SQLException {
        List<String> destinations = List.of("Paris", "Rome");
        when(daoMock.getAllDestinations()).thenReturn(destinations);

        List<String> result = service.getAllDestinations();
        assertEquals(destinations, result);
        verify(daoMock).getAllDestinations();
    }

    @Test
    void testAddTour() throws SQLException {
        TourPackage tour = new TourPackage();
        doNothing().when(daoMock).addTour(tour);

        service.addTour(tour);
        verify(daoMock).addTour(tour);
    }

    @Test
    void testUpdateTour() throws SQLException {
        TourPackage tour = new TourPackage();
        doNothing().when(daoMock).updateTour(tour);

        service.updateTour(tour);
        verify(daoMock).updateTour(tour);
    }

    @Test
    void testDeleteTour() throws SQLException {
        doNothing().when(daoMock).deleteTour(1);

        service.deleteTour(1);
        verify(daoMock).deleteTour(1);
    }
}
