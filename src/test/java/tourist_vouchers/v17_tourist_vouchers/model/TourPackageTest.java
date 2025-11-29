package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TourPackageTest {

    @Test
    void testDefaultConstructor() {
        TourPackage tour = new TourPackage();
        assertNotNull(tour);
    }

    @Test
    void testFullConstructor() {
        TourPackage tour = new TourPackage(
                1,
                "Holiday Special",
                "Paris",
                1200.5,
                7,
                TransportType.BUS,
                FoodType.ALL_INCLUSIVE,
                TourType.EXCURSION
        );

        assertEquals(1, tour.getId());
        assertEquals("Holiday Special", tour.getTitle());
        assertEquals("Paris", tour.getDestination());
        assertEquals(1200.5, tour.getPrice());
        assertEquals(7, tour.getDays());
        assertEquals(TransportType.BUS, tour.getTransport());
        assertEquals(FoodType.ALL_INCLUSIVE, tour.getFoodType());
        assertEquals(TourType.EXCURSION, tour.getTourType());
    }

    @Test
    void testSettersAndGetters() {
        TourPackage tour = new TourPackage();

        tour.setId(10);
        tour.setTitle("Winter Trip");
        tour.setDestination("Rome");
        tour.setPrice(999.99);
        tour.setDays(5);
        tour.setTransport(TransportType.PLANE);
        tour.setFoodType(FoodType.BREAKFAST_ONLY);
        tour.setTourType(TourType.CRUISE);

        assertEquals(10, tour.getId());
        assertEquals("Winter Trip", tour.getTitle());
        assertEquals("Rome", tour.getDestination());
        assertEquals(999.99, tour.getPrice());
        assertEquals(5, tour.getDays());
        assertEquals(TransportType.PLANE, tour.getTransport());
        assertEquals(FoodType.BREAKFAST_ONLY, tour.getFoodType());
        assertEquals(TourType.CRUISE, tour.getTourType());
    }
}
