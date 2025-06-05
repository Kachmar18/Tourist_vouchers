package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TourPackageTest {

    @Test
    void testConstructorAndGetters() {
        TourPackage tour = new TourPackage(
                1,
                "Мальовниче Закарпаття",
                "Ужгород",
                3499.99,
                5,
                TransportType.BUS,
                FoodType.FULL_BOARD,
                TourType.EXCURSION
        );

        assertEquals(1, tour.getId());
        assertEquals("Мальовниче Закарпаття", tour.getTitle());
        assertEquals("Ужгород", tour.getDestination());
        assertEquals(3499.99, tour.getPrice());
        assertEquals(5, tour.getDays());
        assertEquals(TransportType.BUS, tour.getTransport());
        assertEquals(FoodType.FULL_BOARD, tour.getFoodType());
        assertEquals(TourType.EXCURSION, tour.getTourType());
    }

    @Test
    void testSetters() {
        TourPackage tour = new TourPackage(0, "", "", 0.0, 0, null, null, null);

        tour.setId(2);
        tour.setTitle("Карпати весною");
        tour.setDestination("Яремче");
        tour.setPrice(2750.50);
        tour.setDays(4);
        tour.setTransport(TransportType.TRAIN);
        tour.setFoodType(FoodType.BREAKFAST_ONLY);
        tour.setTourType(TourType.REST);

        assertEquals(2, tour.getId());
        assertEquals("Карпати весною", tour.getTitle());
        assertEquals("Яремче", tour.getDestination());
        assertEquals(2750.50, tour.getPrice());
        assertEquals(4, tour.getDays());
        assertEquals(TransportType.TRAIN, tour.getTransport());
        assertEquals(FoodType.BREAKFAST_ONLY, tour.getFoodType());
        assertEquals(TourType.REST, tour.getTourType());
    }
}
