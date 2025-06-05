package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTypeTest {

    @Test
    void testToStringAndDisplayName() {
        for (TourType type : TourType.values()) {
            String expected = type.getDisplayName();
            assertEquals(expected, type.toString(), "Метод toString повинен повертати displayName");
            assertEquals(expected, type.displayNameProperty().get(), "Метод displayNameProperty повинен повертати SimpleStringProperty з displayName");
        }
    }
}
