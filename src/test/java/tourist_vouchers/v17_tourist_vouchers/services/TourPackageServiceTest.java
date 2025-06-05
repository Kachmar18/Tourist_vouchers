package tourist_vouchers.v17_tourist_vouchers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.dao.TourPackageDAO;
import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourPackageServiceTest {
    private TourPackageDAO mockDao;
    private TourPackageService service;
    private TourPackage sampleTour;

    @BeforeEach
    void setUp() {
        mockDao = mock(TourPackageDAO.class);
        service = new TourPackageService(mockDao); // Overloaded constructor required for injection

        sampleTour = new TourPackage(
                1, "Test Tour", "Kyiv", 1000.0, 5,
                TransportType.BUS, FoodType.ALL_INCLUSIVE, TourType.EXCURSION
        );
    }

    @Test
    void testGetAllTours() throws SQLException {
        when(mockDao.getAllTours()).thenReturn(List.of(sampleTour));

        List<TourPackage> tours = service.getAllTours();

        assertEquals(1, tours.size());
        assertEquals("Test Tour", tours.get(0).getTitle());
        verify(mockDao).getAllTours();
    }

    @Test
    void testFilterTours_byPriceAndDestination() throws SQLException {
        when(mockDao.getAllTours()).thenReturn(List.of(sampleTour));

        List<TourPackage> filtered = service.filterTours("Kyiv", 1500.0, null,
                null, null, null);

        assertEquals(1, filtered.size());
        assertEquals("Kyiv", filtered.get(0).getDestination());
    }

    @Test
    void testFilterTours_noMatch() throws SQLException {
        when(mockDao.getAllTours()).thenReturn(List.of(sampleTour));

        List<TourPackage> filtered = service.filterTours("Lviv", null, null,
                null, null, null);

        assertTrue(filtered.isEmpty());
    }

    @Test
    void testGetAllDestinations() throws SQLException {
        when(mockDao.getAllDestinations()).thenReturn(List.of("Kyiv", "Lviv"));

        List<String> destinations = service.getAllDestinations();

        assertEquals(2, destinations.size());
        assertTrue(destinations.contains("Kyiv"));
        verify(mockDao).getAllDestinations();
    }

    @Test
    void testAddTour() throws SQLException {
        doNothing().when(mockDao).addTour(sampleTour);

        service.addTour(sampleTour);

        verify(mockDao).addTour(sampleTour);
    }

    @Test
    void testUpdateTour() throws SQLException {
        doNothing().when(mockDao).updateTour(sampleTour);

        service.updateTour(sampleTour);

        verify(mockDao).updateTour(sampleTour);
    }

    @Test
    void testDeleteTour() throws SQLException {
        doNothing().when(mockDao).deleteTour(1);

        service.deleteTour(1);

        verify(mockDao).deleteTour(1);
    }

    @Test
    void testGetTourById() throws SQLException {
        when(mockDao.getTourById(1)).thenReturn(sampleTour);

        TourPackage result = service.getTourById(1);

        assertNotNull(result);
        assertEquals("Test Tour", result.getTitle());
    }
}
