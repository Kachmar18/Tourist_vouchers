package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransportTypeTest {

    @Test
    void testEnumValues() {
        TransportType[] values = TransportType.values();
        assertEquals(5, values.length);

        assertEquals("Автобус", TransportType.BUS.getDisplayName());
        assertEquals("Літак", TransportType.PLANE.getDisplayName());
        assertEquals("Поїзд", TransportType.TRAIN.getDisplayName());
        assertEquals("Корабель", TransportType.SHIP.getDisplayName());
        assertEquals("Машина", TransportType.CAR.getDisplayName());
    }

    @Test
    void testDisplayNameProperty() {
        TransportType type = TransportType.BUS;
        SimpleStringProperty prop = type.displayNameProperty();
        assertEquals("Автобус", prop.get());
    }

    @Test
    void testToString() {
        assertEquals("Літак", TransportType.PLANE.toString());
        assertEquals("Машина", TransportType.CAR.toString());
    }
}
