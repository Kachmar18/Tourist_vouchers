package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TourTypeTest {

    @Test
    void testEnumValues() {
        TourType[] values = TourType.values();
        assertEquals(5, values.length);

        assertEquals("Відпочинок", TourType.REST.getDisplayName());
        assertEquals("Екскурсії", TourType.EXCURSION.getDisplayName());
        assertEquals("Санаторій", TourType.TREATMENT.getDisplayName());
        assertEquals("Шопінг", TourType.SHOPPING.getDisplayName());
        assertEquals("Круїз", TourType.CRUISE.getDisplayName());
    }

    @Test
    void testDisplayNameProperty() {
        TourType type = TourType.REST;
        SimpleStringProperty prop = type.displayNameProperty();
        assertEquals("Відпочинок", prop.get());
    }

    @Test
    void testToString() {
        assertEquals("Екскурсії", TourType.EXCURSION.toString());
        assertEquals("Круїз", TourType.CRUISE.toString());
    }
}
