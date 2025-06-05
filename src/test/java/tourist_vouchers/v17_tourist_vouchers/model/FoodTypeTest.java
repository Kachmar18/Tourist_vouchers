package tourist_vouchers.v17_tourist_vouchers.model;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTypeTest {

    @Test
    void testToStringAndDisplayName() {
        for (FoodType type : FoodType.values()) {
            assertEquals(type.getDisplayName(), type.toString());
            assertEquals(type.getDisplayName(), type.displayNameProperty().get());
        }
    }
}
