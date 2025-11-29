package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTypeTest {

    @Test
    void testEnumValues() {
        FoodType[] values = FoodType.values();
        assertEquals(4, values.length);
        assertEquals("Все включено", FoodType.ALL_INCLUSIVE.getDisplayName());
        assertEquals("Тільки сніданок", FoodType.BREAKFAST_ONLY.getDisplayName());
        assertEquals("Без харчування", FoodType.NO_MEALS.getDisplayName());
        assertEquals("Повний пансіон", FoodType.FULL_BOARD.getDisplayName());
    }

    @Test
    void testDisplayNameProperty() {
        FoodType type = FoodType.ALL_INCLUSIVE;
        SimpleStringProperty prop = type.displayNameProperty();
        assertEquals("Все включено", prop.get());
    }

    @Test
    void testToString() {
        assertEquals("Все включено", FoodType.ALL_INCLUSIVE.toString());
        assertEquals("Повний пансіон", FoodType.FULL_BOARD.toString());
    }
}
